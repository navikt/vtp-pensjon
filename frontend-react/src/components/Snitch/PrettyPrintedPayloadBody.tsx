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
  return JSON.stringify(JSON.parse(decodeBody(content)), null, "  ");
}

function asXml(content: string): string {
  return xmlFormatter(decodeBody(content));
}

interface BodyProps {
  contentType: string;
  content: string;
}

export default function PrettyPrintedPayloadBody(
  props: BodyProps
): JSX.Element {
  const content = props.content;
  try {
    const contentType = parseContentType(props.contentType).type;
    switch (contentType) {
      case "application/json":
        return <pre>{asJson(content)}</pre>;

      case "application/xml":
        return <pre>{asXml(content)}</pre>;

      case "text/xml":
        return <pre>{asXml(content)}</pre>;

      case "text/html":
        return <pre>{decodeBody(content)}</pre>;

      default:
        return <pre>{content}</pre>;
    }
  } catch (error) {
    console.error("Could not pretty-print/format content", error);
    return <pre>{content}</pre>;
  }
}
