import React, { CSSProperties, useState } from "react";
import { Col, Container, Row } from "react-bootstrap";
import { RequestResponse } from "./types";
import RequestResponseList from "./RequestResponseList";
import PayloadSummary from "./PayloadSummary";
import RequestResponseSummary from "./RequestResponseSummary";
import useSnitchDataStream from "./useSnitchDataStream";
import { Variant } from "react-bootstrap/types";

export function badgeVariant(status: number): Variant {
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

export default () => {
  const [
    selectedRequest,
    setSelectedRequest,
  ] = useState<RequestResponse | null>(null);

  const requests = useSnitchDataStream();

  const scrollable: CSSProperties = {
    overflowX: "scroll",
    height: "calc(100vh - 70px)",
  };

  const requestToShow = selectedRequest != null ? selectedRequest : requests[0];

  return (
    <Container fluid>
      <Row>
        <Col className="w-50" style={scrollable}>
          <RequestResponseList
            requests={requests}
            selectedRequest={requestToShow}
            setSelectedRequest={setSelectedRequest}
          />
        </Col>
        <Col className="w-50" style={scrollable}>
          {requestToShow != null && (
            <>
              <RequestResponseSummary request={requestToShow} />
              <PayloadSummary title="Request" message={requestToShow.request} />
              <PayloadSummary
                title="Response"
                message={requestToShow.response}
              />
            </>
          )}
        </Col>
      </Row>
    </Container>
  );
};
