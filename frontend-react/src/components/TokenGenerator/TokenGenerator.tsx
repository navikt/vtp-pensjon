import React, { useEffect, useState, MouseEvent } from "react";
import {
  Button,
  ButtonGroup,
  Card,
  Col,
  Container,
  Form,
  Row,
} from "react-bootstrap";
import CopyToClipboard from "react-copy-to-clipboard";

type State =
  | {
      type: "NOT_LOADED";
    }
  | {
      type: "LOADING";
    }
  | {
      type: "LOADED";
      token: string;
    }
  | {
      type: "ERROR";
      message: string;
    };

export default () => {
  const [state, setState] = useState<State>({ type: "NOT_LOADED" });

  async function generateToken() {
    try {
      setState({ type: "LOADING" });
      const response = await fetch(
        "/rest/v1/sts/token?grant_type=dfg&scope=fghfgh"
      );
      const token = (await response.json()).access_token;
      setState({ type: "LOADED", token });
    } catch (err) {
      console.error("Could not generate token", err);
      setState({ type: "ERROR", message: err.toString() });
      throw err;
    }
  }

  useEffect(() => {
    return () => {};
  }, []);

  const isLoading = state.type === "LOADING";

  return (
    <Container fluid>
      <h1>Token Generator</h1>
      <Button onClick={generateToken} disabled={isLoading}>
        Generate a token{isLoading ? "..." : ""}
      </Button>
      {state.type === "ERROR" && <div>Error: {state.message}</div>}
      {state.type === "LOADED" && (
        <Card>
          <Card.Body>
            <Form>
              <ButtonGroup aria-label="Basic example">
                <CopyToClipboard text={state.token}>
                  <Button variant="outline-primary" size="sm">
                    Kopier til utklippstavle
                  </Button>
                </CopyToClipboard>

                <Button
                  href={"https://jwt.io/?token=" + state.token}
                  target="_blank"
                >
                  Vis på JWT.io ⧉
                </Button>
              </ButtonGroup>

              <Form.Control
                as="textarea"
                rows={8}
                readOnly
                plaintext
                defaultValue={state.token}
                onClick={(ev: MouseEvent<HTMLTextAreaElement>) =>
                  ev.currentTarget.select()
                }
              />
            </Form>
          </Card.Body>
        </Card>
      )}
    </Container>
  );
};
