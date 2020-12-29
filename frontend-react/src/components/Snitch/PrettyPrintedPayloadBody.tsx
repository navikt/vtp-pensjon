import { parse as parseContentType } from "content-type";
import React from "react";
import xmlFormatter from "xml-formatter";

function decodeBody(value: string): string {
  return decodeURIComponent(
    atob(value)
      .split("")
      .map(function (c) {
        return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
      })
      .join("")
  );
}

function asJson(content: string): string {
  return JSON.stringify(JSON.parse(content), null, "  ");
}

function asXml(content: string): string {
  return xmlFormatter(content);
}

interface BodyProps {
  contentType: string;
  content: string;
}

function asWwwFormUrlencoded(content: string): JSX.Element {
  return (
    <div>
      <dl className="row">
        {content
          .split("&")
          .map((s) => s.split("="))
          .map((s) => [s[0], decodeURIComponent(s[1])])
          .map(([key, value]) => (
            <React.Fragment key={key}>
              <dt className="col-sm-3 col-md-2">{key}</dt>
              <dd className="col-sm-9 col-md-10">{value}</dd>
            </React.Fragment>
          ))}
      </dl>
    </div>
  );
}

export default function PrettyPrintedPayloadBody(
  props: BodyProps
): JSX.Element {
  const content = props.content;
  try {
    const contentType = parseContentType(props.contentType).type;
    switch (contentType) {
      case "application/json":
        return <pre>{asJson(decodeBody(content))}</pre>;

      case "application/xml":
        return <pre>{asXml(decodeBody(content))}</pre>;

      case "text/xml":
        return <pre>{asXml(decodeBody(content))}</pre>;

      case "text/html":
        return <pre>{decodeBody(content)}</pre>;

      case "application/x-www-form-urlencoded":
        return asWwwFormUrlencoded(decodeBody(content));

      default:
        return <pre>{content}</pre>;
    }
  } catch (error) {
    console.error("Could not pretty-print/format content", error);
    return <pre>{content}</pre>;
  }
}
