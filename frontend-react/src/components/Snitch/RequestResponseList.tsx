import { Badge, Table } from "react-bootstrap";
import React from "react";
import { RequestResponse } from "./types";
import FormattedDate from "./FormattedDate";
import { badgeVariant } from "./Snitch";

interface RequestResponseListProps {
  requests: RequestResponse[];
  selectedRequest: RequestResponse | null;
  setSelectedRequest: (request: RequestResponse) => any;
}

export default function RequestResponseList(props: RequestResponseListProps) {
  return (
    <Table striped hover size="sm">
      <thead>
        <tr>
          <th>Timestamp</th>
          <th>Method</th>
          <th>Path</th>
          <th>Status</th>
          <th>HandlerClass</th>
          <th>HandlerMethod</th>
        </tr>
      </thead>
      <tbody>
        {props.requests.map((request, i) => {
          let rowClass =
            request === props.selectedRequest ? "table-primary" : "";

          return (
            <tr
              className={rowClass}
              key={request.id || i}
              style={{ cursor: "pointer" }}
              onClick={() => props.setSelectedRequest(request)}
            >
              <td>
                <FormattedDate time={request.timestamp} />
              </td>
              <td>{request.method}</td>
              <td>{request.path}</td>
              <td>
                <Badge variant={badgeVariant(request.status)}>
                  {request.status}
                </Badge>
              </td>
              <td>{request.handlerClass}</td>
              <td>{request.handlerMethod}</td>
            </tr>
          );
        })}
      </tbody>
    </Table>
  );
}
