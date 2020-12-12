import { Table } from "react-bootstrap";
import React from "react";
import { RequestResponse } from "./types";
import FormattedDate from "./FormattedDate";

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
        </tr>
      </thead>
      <tbody>
        {props.requests.map((request, i) => {
          let rowClass = "";
          if (request === props.selectedRequest) {
            rowClass = "table-primary";
          } else if (request.status >= 400 && request.status <= 499) {
            rowClass = "table-warning";
          } else if (request.status == 500) {
            rowClass = "table-danger";
          }

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
              <td>{request.status}</td>
            </tr>
          );
        })}
      </tbody>
    </Table>
  );
}
