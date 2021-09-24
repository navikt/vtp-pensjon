import { parse as parseContentType } from "content-type";
import React from "react";
import xmlFormatter from "xml-formatter";
import ReactJson from "react-json-view";
import CopyToClipboard from "react-copy-to-clipboard";
import {Button} from "react-bootstrap";

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

function asJson(content: string): JSX.Element {
  return (
    <ReactJson
      src={JSON.parse(content)}
      enableClipboard={false}
      displayDataTypes={false}
      displayObjectSize={false}
      iconStyle={"triangle"}
      style={{ fontSize: "1rem" }}
      theme={"isotope"}
    />
  );
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
        {[...new URLSearchParams(content).entries()].map(([key, value]) => (
          <React.Fragment key={key}>
            <dt className="col-sm-3 col-md-2">{key}</dt>
            <dd className="col-sm-9 col-md-10">{value}</dd>
          </React.Fragment>
        ))}
      </dl>
    </div>
  );
}

function CopyToClipboardButton(props: { text: string }): JSX.Element {
  return <div className="clearfix">
    <div style={{paddingBottom: "6px"}} className="float-right">
      <CopyToClipboard text={props.text}>
        <Button variant="outline-primary" size="sm">
          Kopier til utklippstavle ⧉
        </Button>
      </CopyToClipboard>
    </div>
  </div>
}
export default function PrettyPrintedPayloadBody(
  props: BodyProps
): JSX.Element {
  const content = props.content;
  if (content === undefined || content === null || content.length === 0) {
    return <i style={{ color: "#FF0000" }}>No content</i>;
  }

  try {
    const contentType = parseContentType(props.contentType).type;
    switch (contentType) {
      case "application/json":
        return <>
          <CopyToClipboardButton text={decodeBody(content)}/>
          <pre>{asJson(decodeBody(content))}</pre>
        </>;

      case "application/xml":
        return <>
          <CopyToClipboardButton text={decodeBody(content)}/>
          <pre>{asXml(decodeBody(content))}</pre>
        </>;

      case "text/xml":
        return <>
          <CopyToClipboardButton text={decodeBody(content)}/>
          <pre>{asXml(decodeBody(content))}</pre>
        </>;

      case "text/html":
        return <>
          <CopyToClipboardButton text={decodeBody(content)}/>
          <pre>{decodeBody(content)}</pre>
        </>;

      case "application/x-www-form-urlencoded":
        return asWwwFormUrlencoded(decodeBody(content));

      case "application/pdf":
        return (
          <div className="embed-responsive embed-responsive-1by1">
            <iframe title="Document"
              className="embed-responsive-item"
              src={`data:application/pdf;base64,${content}`}
              frameBorder="0"
              allowFullScreen
            />
          </div>
        );

      default:
        return <pre>{content}</pre>;
    }
  } catch (error) {
    console.error("Could not pretty-print/format content", error);
    return <pre>{content}</pre>;
  }
}
