import React from "react";
import { Badge, Card } from "react-bootstrap";
import { RequestResponse } from "./types";
import FormattedDate from "./FormattedDate";
import { badgeVariant } from "./Snitch";

interface SummaryProps {
  request: RequestResponse;
}
export default function RequestResponseSummary(props: SummaryProps) {
  const { request } = props;
  return (
    <Card>
      <Card.Header>
        {request.path}

        <div className="float-right">
          <Badge variant="primary">{request.method}</Badge>{" "}
          <Badge variant={badgeVariant(request.status)}>{request.status}</Badge>
        </div>
      </Card.Header>

      <Card.Body>
        <dl className="row">
          <dt className="col-sm-3 col-md-2">Timestamp</dt>
          <dd className="col-sm-9 col-md-10">
            <FormattedDate time={request.timestamp} />
          </dd>
          <dt className="col-sm-3 col-md-2">URL</dt>
          <dd className="col-sm-9 col-md-10">{request.url}</dd>
          {request.handler != null && (
            <>
              <dt className="col-sm-3 col-md-2">Handler</dt>
              <dd className="col-sm-9 col-md-10">{request.handler}</dd>
            </>
          )}
          {request.exception != null && (
            <>
              <dt className="col-sm-3 col-md-2">Exception</dt>
              <dd className="col-sm-9 col-md-10">{request.exception}</dd>
            </>
          )}
        </dl>
        {request.stackTrace != null && (
          <>
            <h5>Stacktrace</h5>
            <pre>{request.stackTrace}</pre>
          </>
        )}
      </Card.Body>
    </Card>
  );
}
