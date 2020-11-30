import React, { useEffect, useState } from 'react';
import { Stomp } from '@stomp/stompjs';
import {DateTimeFormatter, LocalDateTime, Month} from '@js-joda/core';
import {Card, Col, Container, Row, Table} from "react-bootstrap";

function parseDate(input): LocalDateTime {
    return LocalDateTime.parse(input);
    /*
    return LocalDateTime.of(
        input.year,
        input.monthValue,
        input.dayOfMonth,
        input.hour,
        input.minute,
        input.second,
        input.nano
    );
     */
}

function decodeBody(value) {
    if (value != null) {
        return  decodeURIComponent(atob(value).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
    } else {
        return null
    }
}

const dateFormatter = DateTimeFormatter.ofPattern('HH:mm:ss.SSS');

function Summary({request}) {
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

function Payload({ title, message }) {
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
                <pre>{decodeBody(message.content)}</pre>
            </Card.Body>
        </Card>
    );
}

export default () => {
    const [requests, setRequests] = useState([]);
    const [selectedRequest, setSelectedRequest] = useState(null);

    useEffect(() => {
        console.log('Running effect');

        const url = 'ws://localhost:8060/api/ws';
        const client = Stomp.client(url);

        let subscription;
        client.onConnect = () => {
            subscription = client.subscribe('/topic/snitch', message => {
                const reqres = JSON.parse(message.body);
                console.log('got reqres', reqres);
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
    const scrollable = {
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
