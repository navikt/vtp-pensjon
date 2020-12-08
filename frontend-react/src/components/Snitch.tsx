import React, {CSSProperties, useEffect, useState} from 'react';
import {Stomp, StompSubscription} from '@stomp/stompjs';
import {DateTimeFormatter, LocalDateTime} from '@js-joda/core';
import {Card, Col, Container, Row, Table} from "react-bootstrap";
import xmlFormatter from 'xml-formatter';
import {parse as parseContentType} from 'content-type';


interface Payload {
    headers: Map<string, string[]>;
    contentType: string | null;
    contentLength: number | null;
    content: string | null;
}

interface RequestResponse {
    id: string;
    timestamp: string;
    path: string;
    url: string;
    method: string;
    status: number;
    handler: string | null;
    exception: string | null;
    stackTrace: string | null;

    request: Payload;

    response: Payload;
}

function statusColor(status: number): string {
    if (status >= 200 && status <= 299) {
        return 'success';
    } else if (status >= 400 && status <= 499) {
        return 'warning';
    } else if (status >= 500 && status <= 599) {
        return 'danger';
    } else {
        return 'primary';
    }
}

function parseDate(input: string): LocalDateTime {
    return LocalDateTime.parse(input);
}

function decodeBody(value: string): string {
    return decodeURIComponent(atob(value).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
}

const dateFormatter = DateTimeFormatter.ofPattern('HH:mm:ss.SSS');

interface SummaryProps {
    request: RequestResponse
}
function Summary(props: SummaryProps) {
    const { request } = props;
    return (
        <Card>
            <Card.Header>
                {request.path}

                <div className="float-right">
                    <span className="badge badge-primary">{request.method}</span>
                    &nbsp;
                    <span className={`badge badge-${statusColor(request.status)}`}>{request.status}</span>
                </div>
            </Card.Header>

            <Card.Body>
                <dl className="row">
                    <dt className="col-sm-3 col-md-2">Timestamp</dt>
                    <dd className="col-sm-9 col-md-10">{parseDate(request.timestamp).format(dateFormatter)}</dd>
                    <dt className="col-sm-3 col-md-2">URL</dt>
                    <dd className="col-sm-9 col-md-10">{request.url}</dd>
                    {request.handler != null && <>
                        <dt className="col-sm-3 col-md-2">Handler</dt>
                        <dd className="col-sm-9 col-md-10">{request.handler}</dd>
                    </>}
                    {request.exception != null && <>
                        <dt className="col-sm-3 col-md-2">Exception</dt>
                        <dd className="col-sm-9 col-md-10">{request.exception}</dd>
                    </>}
                </dl>
            </Card.Body>
        </Card>
    );
}

interface PayloadBodyProps {
    payload: Payload
}

function asJson(content: string): string {
    return JSON.stringify(JSON.parse(decodeBody(content)), null, '  ')
}

function asXml(content: string): string {
    return xmlFormatter(decodeBody(content))
}

function formatBody(contentType: string, content: string) {
    switch (parseContentType(contentType).type) {
        case 'application/json':
            return <pre>{asJson(content)}</pre>

        case 'application/xml':
            return <pre>{asXml(content)}</pre>;

        case 'text/xml':
            return <pre>{asXml(content)}</pre>;

        case 'text/html':
            return <pre>{decodeBody(content)}</pre>;

        default:
            return null;
    }
}

function PayloadBody(props: PayloadBodyProps): JSX.Element | null {
    const { contentType, contentLength, content } = props.payload

    if (contentType != null && contentLength != null && contentLength > 0 && content != null) {
        let body: JSX.Element | null = formatBody(contentType, content);
        if (body != null) {
            return <Card.Body className="pt-0">{body}</Card.Body>
        } else {
            return null
        }
    } else {
        return null
    }
}

interface PayloadProps {
    title: string,
    message: Payload
}
function Payload(props: PayloadProps) {
    const { title, message } = props;
    let contentBadges;
    if (message.contentLength && message.contentLength > 0) {
        contentBadges = <div className="float-right">
            <span className="badge badge-primary">{message.contentType}</span>
            &nbsp;
            <span className={`badge badge-primary`}>{message.contentLength}</span>
        </div>
    }

    return (
        <Card>
            <Card.Header>
                {title}
                {contentBadges}
            </Card.Header>
            <Card.Body className="pb-0">
                <dl className="row">
                    {Object.entries(message.headers).map(([key, value]) =>
                        <React.Fragment key={key}>
                            <dt className="col-sm-3 col-md-2">{key}</dt>
                            <dd className="col-sm-9 col-md-10">{value}</dd>
                        </React.Fragment>
                    )}
                </dl>
            </Card.Body>
            <PayloadBody payload={message} />
        </Card>
    );
}

/**
 * Creates a unique sorted array of the given RequestResponse array.
 */
function uniqueAndSorted(requestResponses: RequestResponse[]): RequestResponse[] {
    return Array.from(new Set(requestResponses)).sort((r1, r2) => r1.timestamp > r2.timestamp ? -1 : 1);
}

export default () => {
    const [requests, setRequests] = useState<RequestResponse[]>([]);
    const [selectedRequest, setSelectedRequest] = useState<RequestResponse | null>(null);

    useEffect(() => {
        // Start subscription before fetching existing data. A reverse order may loose data between existing data
        // is fetched and a subscription is started
        const websocketUrl = `${window.location.protocol.replace('http', 'ws')}//${window.location.hostname}:${window.location.port}/api/ws`;
        const client = Stomp.client(websocketUrl);
        client.debug = () => {}; // do nothing

        let subscription: StompSubscription;
        client.onConnect = () => {
            subscription = client.subscribe('/topic/snitch', message => {
                setRequests(state => {
                    let requestResponses = uniqueAndSorted([JSON.parse(message.body), ...state]);
                    setSelectedRequest(prevState => prevState != null ? prevState : requestResponses[0]);
                    return requestResponses;
                });
            });
        };
        client.onStompError = error => {
            console.error('Stomp error', error);
        };

        client.activate();

        fetch('/data/requestResponses')
            .then(response => response.json())
            .then(result => setRequests(state => {
                let requestResponses = uniqueAndSorted([...result._embedded.requestResponses, ...state]);
                setSelectedRequest(prevState => prevState != null ? prevState : requestResponses[0]);
                return requestResponses
            }));

        return () => {
            subscription && subscription.unsubscribe();
            client.deactivate();
        }
    }, []);
    const scrollable: CSSProperties = {
        overflowX: 'scroll',
        height: 'calc(100vh - 70px)'
    };
    return (
        <Container fluid>
            <Row>
                <Col className="w-50" style={scrollable}>
                    <Table striped hover size="sm">
                        <thead>
                        <tr>
                            <th>Timestamp</th>
                            <th>Method</th>
                            <th>Path</th>
                            <th>Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        {requests.map((request, i) => {
                            const time = parseDate(request.timestamp).format(dateFormatter);
                            let rowClass = '';
                            if (request.status >= 400 && request.status <= 499) {
                                rowClass = 'table-warning';
                            } else if (request.status == 500) {
                                rowClass = 'table-danger';
                            }

                            return (
                                <tr className={rowClass} key={request.id || i} style={{ cursor: 'pointer' }} onClick={() => setSelectedRequest(request)}>
                                    <td>{time}</td>
                                    <td>{request.method}</td>
                                    <td>{request.path}</td>
                                    <td>{request.status}</td>
                                </tr>
                            );
                        })}
                        </tbody>

                    </Table>

                </Col>
                <Col className="w-50" style={scrollable}>
                    {selectedRequest != null && <>
                        <Summary request={selectedRequest} />
                        <Payload title="Request" message={selectedRequest.request} />
                        <Payload title="Response" message={selectedRequest.response} />
                    </>}
                </Col>
            </Row>
        </Container>
    );
}
