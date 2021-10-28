import React from "react";
import { RequestResponse } from "./types";
import {decodeBody} from "./PrettyPrintedPayloadBody";
import CopyToClipboard from "react-copy-to-clipboard";
import {Button} from "react-bootstrap";
import {Clipboard} from "react-bootstrap-icons";
interface SummaryProps {
  request: RequestResponse;
}


function CopyToClipboardButton(props: { text: string }): JSX.Element {
  return <div className="clearfix">
    <div style={{paddingBottom: "6px"}} className="float-end">
      <CopyToClipboard text={props.text}>
        <Button variant="outline-primary" size="sm">
          Copy Curl Command to Clipboard <Clipboard/>
        </Button>
      </CopyToClipboard>
    </div>
  </div>
}

function curlCommand(request: RequestResponse) {
  let headers = ""
  if (request.request.headers) {
    Object.entries(request.request.headers).forEach(([key, value]) => {
      if (key !== "host") {
        headers += ` -H "${key}: ${value}"`
      }
        }
    )
  }

  let data
  if (request.request.content === undefined || request.request.content === null || request.request.content.length === 0) {
    data = ""
  } else {
    let decoded = decodeBody(request.request.content)
    data = `-d '${decoded}'`
  }

  return `curl -X ${request.method} ${headers} ${data} ${request.url}`
}

function decode(string: string | null): string | null {
  if (string) {
    try {
      return decodeURI(string)
    } catch (e) {
      return string
    }
  } else {
    return string
  }
}

export default function RequestResponseSummary(props: SummaryProps) {
  const { request } = props;

  let queryParameters
  if (props.request.queryString) {
    queryParameters = (<div className="pt-3">
      <h5>Query Parameters</h5>
      <hr/>
      <dl className="row mb-0">
        {props.request.queryString.split("&").map(query => {
          let [key, value] = query.split("=");
          return <>
          <dt className="col-sm-3 col-md-2">{key}</dt>
          <dd className="col-sm-9 col-md-10">{decode(value)}</dd>
          </>
        })
        }
      </dl>
      </div>)
  } else {
    queryParameters = <></>
  }
  return <div style={{backgroundColor: "white"}}>
    <dl className="row mb-0">
      <dt className="col-sm-3 col-md-2">URL</dt>
      <dd className="col-sm-9 col-md-10">{request.url}</dd>
      {request.handler != null && (
          <>
            <dt className="col-sm-3 col-md-2">Handler</dt>
            <dd className="col-sm-9 col-md-10">{request.handler}</dd>
            <dt className="col-sm-3 col-md-2">Class</dt>
            <dd className="col-sm-9 col-md-10">{request.handlerClass}</dd>
            <dt className="col-sm-3 col-md-2">Method</dt>
            <dd className="col-sm-9 col-md-10">{request.handlerMethod}</dd>
          </>
      )}
      {request.exception != null && (
          <>
            <dt className="col-sm-3 col-md-2">Exception</dt>
            <dd className="col-sm-9 col-md-10">{request.exception}</dd>
          </>
      )}
    </dl>
    {queryParameters}
    {request.stackTrace != null && (
        <>
          <h5>Stacktrace</h5>
          <pre>{request.stackTrace}</pre>
        </>
    )}
    <CopyToClipboardButton text={curlCommand(request)}/>
  </div>;
}
