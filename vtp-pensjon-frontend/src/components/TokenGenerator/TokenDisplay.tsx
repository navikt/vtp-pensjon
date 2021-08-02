import { Button, ButtonGroup, Card, Form } from "react-bootstrap";
import React, { MouseEvent } from "react";
import ReactJson from "react-json-view";
import CopyToClipboard from "react-copy-to-clipboard";

function EncodedToken(props: { token: string }) {
  return (
    <Card.Body>
      <Form>
        <Form.Control
          as="textarea"
          readOnly
          plaintext
          defaultValue={props.token}
          onClick={(ev: MouseEvent<HTMLTextAreaElement>) =>
            ev.currentTarget.select()
          }
        />
      </Form>
    </Card.Body>
  );
}

function asDateString(secondsSinceEpoch: number) {
  if (secondsSinceEpoch != null) {
    const d = new Date(0);
    d.setUTCSeconds(secondsSinceEpoch);
    return d.toString();
  } else {
    return undefined;
  }
}

function DecodedToken(props: { header: any; payload: any }) {
  let issuedAt = asDateString(props.payload.iat);
  let expires = asDateString(props.payload.exp);

  return (
    <>
      {(issuedAt !== undefined || expires !== undefined) && (
        <Card.Body>
          <dl className="row">
            {issuedAt !== undefined && (
              <>
                <dt className="col-md-2">Issued at</dt>
                <dd className="col-md-10">{issuedAt}</dd>
              </>
            )}
            {expires !== undefined && (
              <>
                <dt className="col-md-2">Expires</dt>
                <dd className="col-md-10">{expires}</dd>
              </>
            )}
          </dl>
        </Card.Body>
      )}
      <Card.Body>
        <ReactJson
          name={"header"}
          src={props.header}
          enableClipboard={false}
          displayDataTypes={false}
          displayObjectSize={true}
          iconStyle={"triangle"}
          style={{ fontSize: "1rem" }}
        />
      </Card.Body>
      <Card.Body>
        <ReactJson
          name={"payload"}
          src={props.payload}
          enableClipboard={false}
          displayDataTypes={false}
          displayObjectSize={true}
          iconStyle={"triangle"}
          style={{ fontSize: "1rem" }}
        />
      </Card.Body>
    </>
  );
}

export function TokenDisplay(props: { token: string }) {
  return (
    <div>
      <Card>
        <Card.Header>
          <div className="d-flex align-items-center">
            <h3 className="mr-auto">Token</h3>
            <ButtonGroup aria-label="Basic example">
              <Button
                variant="outline-secondary"
                href={"https://jwt.io/?token=" + props.token}
                target="_blank"
              >
                Vis på JWT.io
              </Button>
              <CopyToClipboard text={props.token}>
                <Button variant="outline-primary" size="sm">
                  Kopier til utklippstavle ⧉
                </Button>
              </CopyToClipboard>
            </ButtonGroup>
          </div>
        </Card.Header>

        <EncodedToken token={props.token} />
        <DecodedToken
          header={JSON.parse(atob(props.token.split(".")[0]))}
          payload={JSON.parse(atob(props.token.split(".")[1]))}
        />
      </Card>
    </div>
  );
}
