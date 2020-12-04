import React, {CSSProperties, useEffect, useState} from 'react';
import {Stomp, StompSubscription} from '@stomp/stompjs';
import {DateTimeFormatter, LocalDateTime, Month} from '@js-joda/core';
import {Card, Col, Container, Row, Table} from "react-bootstrap";

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

function parseDate(input: string): LocalDateTime {
    return LocalDateTime.parse(input);
}

function decodeBody(value: string | null) {
    if (value != null) {
        return  decodeURIComponent(atob(value).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
    } else {
        return ''
    }
}

const dateFormatter = DateTimeFormatter.ofPattern('HH:mm:ss.SSS');

interface SummaryProps {
    request: RequestResponse
}
function Summary(props: SummaryProps) {
    const { request } = props;
    return (
        <Card>
            <div className="card-header">
                {request.method} {request.path}
            </div>
            <Card.Body>
                <dl className="row">
                    <dt className="col-sm-3">Timestamp</dt>
                    <dd className="col-sm-9">{parseDate(request.timestamp).format(dateFormatter)}</dd>
                    <dt className="col-sm-3">URL</dt>
                    <dd className="col-sm-9">{request.url}</dd>
                    {request.handler != null && <>
                        <dt className="col-sm-3">Handler</dt>
                        <dd className="col-sm-9">{request.handler}</dd>
                    </>}
                    {request.exception != null && <>
                        <dt className="col-sm-3">Exception</dt>
                        <dd className="col-sm-9">{request.exception}</dd>
                    </>}
                </dl>
            </Card.Body>
        </Card>
    );
}

interface PayloadBodyProps {
    payload: Payload
}
function PayloadBody(props: PayloadBodyProps) {
    const message = props.payload;

    const contentType: string | null = message.contentType;

    switch (contentType) {
        case 'application/json': return <pre>{JSON.stringify(JSON.parse(decodeBody(message.content)), null, '  ')}</pre>;
        default: return <pre>{decodeBody(message.content)}</pre>;
    }
}

interface PayloadProps {
    title: string,
    message: Payload
}
function Payload(props: PayloadProps) {
    const { title, message } = props;
    return (
        <Card>
            <div className="card-header">
                {title}
            </div>
            <Card.Body>
                <dl className="row">
                    {Object.entries(message.headers).map(([key, value]) =>
                        <>
                            <dt className="col-sm-3">{key}</dt>
                            <dd className="col-sm-9">{value}</dd>
                        </>
                    )}
                </dl>
            </Card.Body>
            <Card.Body>
                <PayloadBody payload={message} />
            </Card.Body>
        </Card>
    );
}

export default () => {
    const [requests, setRequests] = useState<RequestResponse[]>([]);
    const [selectedRequest, setSelectedRequest] = useState<RequestResponse | null>(null);

    useEffect(() => {
        const websocketUrl = `${window.location.protocol.replace('http', 'ws')}//${window.location.hostname}:${window.location.port}/api/ws`;

        const client = Stomp.client(websocketUrl);

        client.debug = () => {}; // do nothing

        let subscription: StompSubscription;
        client.onConnect = () => {
            subscription = client.subscribe('/topic/snitch', message => {
                const reqres = JSON.parse(message.body);
                setRequests(state => [...state, reqres]);
            });
        };
        client.onStompError = error => {
            console.error('Stomp error', error);
        };

        client.activate();

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
                                <tr className={rowClass} key={request.id || i} style={{ cursor: 'pointer' }} onClick={ev => setSelectedRequest(request)}>
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
