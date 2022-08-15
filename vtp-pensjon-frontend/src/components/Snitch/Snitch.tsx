import React, {CSSProperties, useState} from "react";
import {Col, Container, Row} from "react-bootstrap";
import {RequestFilters, RequestResponse} from "./types";
import RequestResponseList from "./RequestResponseList";
import PayloadSummary from "./PayloadSummary";
import useSnitchDataStream from "./useSnitchDataStream";
import {Variant} from "react-bootstrap/types";

export function badgeBackground(status: number): Variant {
    if (status >= 200 && status <= 299) {
        return "success";
    } else if (status >= 400 && status <= 499) {
        return "warning";
    } else if (status >= 500 && status <= 599) {
        return "danger";
    } else {
        return "primary";
    }
}

const Snitch = () => {
    const [selectedRequest, setSelectedRequest] =
        useState<RequestResponse | null>(null);

    const [filters, setFilters] = useState<RequestFilters>(new RequestFilters([], [], []));

    const [requests, clear, ignorePath] = useSnitchDataStream();

    const scrollable: CSSProperties = {
        overflowX: "scroll",
        height: "calc(100vh - 70px)",
    };

    const requestToShow = selectedRequest != null ? selectedRequest : requests[0];

    return (
        <Container fluid>
            <Row className="pt-2">
                <Col sm={6} md={6} lg={6} xl={4} xxl={4} style={scrollable}>
                    <RequestResponseList
                        requests={requests}
                        selectedRequest={requestToShow}
                        setSelectedRequest={setSelectedRequest}
                        onClear={clear}
                        onIgnorePath={ignorePath}
                        filters={filters}
                        setFilters={setFilters}
                    />
                </Col>
                {requestToShow != null && (
                    <Col sm={6} md={6} lg={6} xl={8} xxl={8}>
                        <Row>
                            <Col sm={12} md={12} lg={12} xl={6} xxl={6}>
                                <PayloadSummary title="Request" message={requestToShow.request}/>
                            </Col>
                            <Col sm={12} md={12} lg={12} xl={6} xxl={6}>
                                <div className="pt-2 d-xl-none"/>
                                <PayloadSummary
                                    title="Response"
                                    message={requestToShow.response}
                                />
                            </Col>
                        </Row>
                    </Col>
                )}
            </Row>
        </Container>
    );
};

export default Snitch
