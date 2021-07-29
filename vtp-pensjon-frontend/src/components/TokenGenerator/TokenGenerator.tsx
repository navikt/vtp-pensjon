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

function TokenPanel(props: { generateToken: () => Promise<string> }) {
  const [state, setState] = useState<State>({ type: "NOT_LOADED" });

  async function generate() {
    try {
      setState({ type: "LOADING" });
      const token = await props.generateToken();
      setState({ type: "LOADED", token });
    } catch (err) {
      console.error("Could not generate token", err);
      setState({ type: "ERROR", message: err.toString() });
      throw err;
    }
  }

  const isLoading = state.type === "LOADING";

  return (
    <div>
      <Button onClick={generate} disabled={isLoading}>
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
    </div>
  );
}

async function generateOpenAMToken() {
  try {
    const response = await fetch(
      "/rest/v1/sts/token?grant_type=dfg&scope=fghfgh"
    );
    return (await response.json()).access_token;
  } catch (err) {
    console.error("Could not generate token", err);
    throw err;
  }
}

async function generateAzureADToken() {
  try {
    const response = await fetch("/rest/AzureAd/123456/mock-token", {
      method: "post",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: "client_id=whatever",
    });
    return await response.text();
  } catch (err) {
    console.error("Could not generate token", err);
    throw err;
  }
}

export default () => {
  return (
    <Container fluid>
      <h1>Token Generator</h1>
      <Row>
        <Col>
          <h2>STS token</h2>
          <TokenPanel generateToken={generateOpenAMToken} />
        </Col>
        <Col>
          <h2>OpenAM token</h2>
          <TokenPanel generateToken={generateOpenAMToken} />
        </Col>
        <Col>
          <h2>Azure AD token</h2>
          <TokenPanel generateToken={generateAzureADToken} />
        </Col>
      </Row>
    </Container>
  );
};
