import React, { FormEvent, useEffect, useState } from "react";
import {
  Button,
  Col,
  Container,
  Form,
  FormControl,
  Row,
} from "react-bootstrap";
import { TokenDisplay } from "./TokenDisplay";
import { CommandDisplay } from "./CommandDisplay";
import {
  azureAdResponseMapper,
  generateAzureADRequest,
  generateOpenAMRequest,
  generateStsRequest,
  openAmResponseMapper,
  RequestParameters,
  stsResponeMapper,
} from "./RequestSuppliers";
import { generateCommand } from "./CommandLineGenerator";

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

function TokenPanel(props: {
  useUsername: boolean;
  useClientId: boolean;
  useTenantId: boolean;
  requestSupplier: (
    requestParameters: RequestParameters
  ) => Request | undefined;
  responseMapper: (response: Response) => Promise<string>;
  defaultClientId: string | undefined;
  defaultUsername: string | undefined;
  defaultTenantId: string | undefined;
}) {
  const [state, setState] = useState<State>({ type: "NOT_LOADED" });
  const [clientId, setClientId] = useState<string | undefined>(
    props.defaultClientId
  );
  const [username, setUsername] = useState<string | undefined>(
    props.defaultUsername
  );
  const [tenantId, setTenantId] = useState<string | undefined>(
    props.defaultTenantId
  );
  const [command, setCommand] = useState<string | undefined>(undefined);

  function getRequest() {
    return props.requestSupplier({
      username: username,
      clientId: clientId,
      tenantId: tenantId,
    });
  }

  useEffect(() => {
    (async function () {
      setCommand(await generateCommand(getRequest()));
    })();
  }, [clientId, username, tenantId]);

  async function generateToken(request: Request): Promise<string> {
    const response = await fetch(request);
    if (response.status / 100 != 2) {
      throw Error(
        "status: " +
          response.statusText +
          ", message: " +
          (await response.text())
      );
    }
    return props.responseMapper(response);
  }

  async function generate() {
    try {
      setState({ type: "LOADING" });
      const request = getRequest();
      if (request !== undefined) {
        const token = await generateToken(request);
        setState({
          type: "LOADED",
          token: token,
        });
      }
    } catch (err) {
      console.error("Could not generate token", err);
      setState({ type: "ERROR", message: err.toString() });
    }
  }

  function handleSubmit(evt: FormEvent) {
    generate();
    evt.preventDefault();
  }

  const isLoading = state.type === "LOADING";

  const isButtonDisabled = isLoading || getRequest() === undefined;

  return (
    <>
      <Form onSubmit={handleSubmit} style={{ marginBottom: "12px" }}>
        {props.useTenantId && (
          <Form.Group className="mb-3" controlId="tenantId">
            <Form.Label>TenantId</Form.Label>
            <FormControl
              placeholder="TenantId"
              aria-label="TenantId"
              aria-describedby="generate-button"
              value={tenantId}
              onChange={(e) => setTenantId(e.target.value)}
            />
          </Form.Group>
        )}
        {props.useClientId && (
          <Form.Group className="mb-3" controlId="clientId">
            <Form.Label>ClientId</Form.Label>
            <FormControl
              placeholder="ClientId"
              aria-label="ClientId"
              aria-describedby="generate-button"
              value={clientId}
              onChange={(e) => setClientId(e.target.value)}
            />
          </Form.Group>
        )}
        {props.useUsername && (
          <Form.Group className="mb-3" controlId="username">
            <Form.Label>Username</Form.Label>
            <FormControl
              placeholder="Username"
              aria-label="Username"
              aria-describedby="generate-button"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </Form.Group>
        )}
        <Button
          variant="outline-secondary"
          id="generate-button"
          type="submit"
          disabled={isButtonDisabled}
        >
          Generate token
        </Button>
      </Form>
      <CommandDisplay command={command} />
      <div style={{ marginBottom: "12px" }} />
      {state.type === "ERROR" && <div>{state.message}</div>}
      {state.type === "LOADED" && <TokenDisplay token={state.token} />}
    </>
  );
}

export default () => {
  return (
    <Container fluid>
      <Row>
        <Col>
          <h2>STS token</h2>
          <TokenPanel
            useUsername={true}
            useClientId={false}
            useTenantId={false}
            defaultClientId={"psak"}
            defaultUsername={"saksbeh"}
            defaultTenantId={undefined}
            requestSupplier={generateStsRequest}
            responseMapper={stsResponeMapper}
          />
        </Col>
        <Col>
          <h2>OpenAM token</h2>
          <TokenPanel
            useClientId={true}
            useUsername={true}
            useTenantId={false}
            defaultClientId={"psak"}
            defaultUsername={"saksbeh"}
            defaultTenantId={undefined}
            requestSupplier={generateOpenAMRequest}
            responseMapper={openAmResponseMapper}
          />
        </Col>
        <Col>
          <h2>Azure AD token</h2>
          <TokenPanel
            useClientId={true}
            useUsername={true}
            useTenantId={true}
            defaultClientId={"psak"}
            defaultUsername={"saksbeh"}
            defaultTenantId={"966ac572-f5b7-4bbe-aa88-c76419c0f851"}
            requestSupplier={generateAzureADRequest}
            responseMapper={azureAdResponseMapper}
          />
        </Col>
      </Row>
    </Container>
  );
};
