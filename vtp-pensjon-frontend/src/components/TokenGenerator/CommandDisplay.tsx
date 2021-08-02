import { Button, ButtonGroup, Card } from "react-bootstrap";
import CopyToClipboard from "react-copy-to-clipboard";
import React from "react";

export function CommandDisplay(props: { command: string | undefined }) {
  return (
    <Card>
      <Card.Header>
        <div className="d-flex align-items-center">
          <h3 className="mr-auto">Comand</h3>
          <ButtonGroup>
            {props.command !== undefined ? (
              <CopyToClipboard options={{ debug: true }} text={props.command}>
                <Button variant="outline-primary" size="sm">
                  Kopier til utklippstavle ⧉
                </Button>
              </CopyToClipboard>
            ) : (
              <Button disabled={true} variant="outline-primary" size="sm">
                Kopier til utklippstavle ⧉
              </Button>
            )}
          </ButtonGroup>
        </div>
      </Card.Header>
      <Card.Body className={"text-light bg-dark"}>
        <p className="text-monospace">{props.command}</p>
      </Card.Body>
    </Card>
  );
}
