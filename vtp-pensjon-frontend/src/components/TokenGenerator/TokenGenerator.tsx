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
import { generateCommand } from "./CommandLineGenerator";
import {
  maskinportenRequestSupplier,
  maskinportenResponseMapper,
} from "./support/MaskinportenSupport";
import {
  azureADRequestSupplier,
  azureAdResponseMapper,
} from "./support/AzureADSupport";
import {
  openAMRequestSupplier,
  openAmResponseMapper,
} from "./support/OpenAMSupport";
import { stsRequestSupplier, stsResponseMapper } from "./support/StsSupport";
import { RequestParameters } from "./RequestParameters";

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

interface Foo {
  default?: string;
}

function TokenPanel(props: {
  clientId?: Foo;
  resource?: Foo;
  scope?: Foo;
  tenantId?: Foo;
  username?: Foo;
  requestSupplier: (
    requestParameters: RequestParameters
  ) => Request | undefined;
  responseMapper: (response: Response) => Promise<string>;
}) {
  const [state, setState] = useState<State>({ type: "NOT_LOADED" });
  const [clientId, setClientId] = useState<string | undefined>(
    props.clientId?.default
  );
  const [username, setUsername] = useState<string | undefined>(
    props.username?.default
  );
  const [resource, setResource] = useState<string | undefined>(
    props.resource?.default
  );
  const [scope, setScope] = useState<string | undefined>(props.scope?.default);
  const [tenantId, setTenantId] = useState<string | undefined>(
    props.tenantId?.default
  );
  const [command, setCommand] = useState<string | undefined>(undefined);

  function getRequest() {
    return props.requestSupplier({
      clientId: clientId,
      resource: resource,
      scope: scope,
      tenantId: tenantId,
      username: username,
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
        {props.tenantId && (
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
        {props.clientId && (
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
        {props.username && (
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
        {props.resource && (
          <Form.Group className="mb-3" controlId="resource">
            <Form.Label>Resource</Form.Label>
            <FormControl
              placeholder="Resource"
              aria-label="Resource"
              aria-describedby="generate-button"
              value={resource}
              onChange={(e) => setResource(e.target.value)}
            />
          </Form.Group>
        )}
        {props.scope && (
          <Form.Group className="mb-3" controlId="scope">
            <Form.Label>Scope</Form.Label>
            <FormControl
              placeholder="Scope"
              aria-label="Scope"
              aria-describedby="generate-button"
              value={scope}
              onChange={(e) => setScope(e.target.value)}
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
            username={{ default: "saksbeh" }}
            clientId={{ default: "psak" }}
            requestSupplier={stsRequestSupplier}
            responseMapper={stsResponseMapper}
          />
        </Col>
        <Col>
          <h2>OpenAM token</h2>
          <TokenPanel
            username={{ default: "saksbeh" }}
            clientId={{ default: "psak" }}
            requestSupplier={openAMRequestSupplier}
            responseMapper={openAmResponseMapper}
          />
        </Col>
        <Col>
          <h2>Azure AD token</h2>
          <TokenPanel
            username={{ default: "saksbeh" }}
            clientId={{ default: "psak" }}
            scope={{}}
            tenantId={{ default: "966ac572-f5b7-4bbe-aa88-c76419c0f851" }}
            requestSupplier={azureADRequestSupplier}
            responseMapper={azureAdResponseMapper}
          />
        </Col>
        <Col>
          <h2>Maskinporten token</h2>
          <TokenPanel
            clientId={{ default: "889640782" }}
            resource={{}}
            scope={{ default: "nav:pensjon/v1/tpregisteret" }}
            requestSupplier={maskinportenRequestSupplier}
            responseMapper={maskinportenResponseMapper}
          />
        </Col>
      </Row>
    </Container>
  );
};
