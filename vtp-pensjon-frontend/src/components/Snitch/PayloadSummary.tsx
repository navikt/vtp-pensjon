import {Badge, Card} from "react-bootstrap";
import React from "react";
import { Payload } from "./types";
import PrettyPrintedPayloadBody from "./PrettyPrintedPayloadBody";

interface PayloadBodyProps {
  payload: Payload;
}
function PayloadBody(props: PayloadBodyProps): JSX.Element | null {
  const { contentType, contentLength, content } = props.payload;

  if (
    contentType != null &&
    contentLength != null &&
    contentLength > 0 &&
    content != null
  ) {
    return (
      <Card.Body className="pt-0">
        <hr />
        <PrettyPrintedPayloadBody contentType={contentType} content={content} />
      </Card.Body>
    );
  } else {
    return null;
  }
}

interface PayloadProps {
  title: string;
  message: Payload;
}
export default function PayloadSummary(props: PayloadProps) {
  const { title, message } = props;
  let contentBadges;
  if (message.contentLength && message.contentLength > 0) {
    contentBadges = (
      <div className="float-right">
        <Badge variant="primary">{message.contentType}</Badge>{" "}
        <Badge variant="primary">{message.contentLength}</Badge>
      </div>
    );
  }

  return (
    <Card>
      <Card.Header>
        {title}
        {contentBadges}
      </Card.Header>
      <Card.Body className="pb-0">
        <dl className="row">
          {Object.entries(message.headers).map(([key, value]) => (
            <React.Fragment key={key}>
              <dt className="col-sm-3 col-md-2">{key}</dt>
              <dd className="col-sm-9 col-md-10">{value}</dd>
            </React.Fragment>
          ))}
        </dl>
      </Card.Body>
      <PayloadBody payload={message} />
    </Card>
  );
}
