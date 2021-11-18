import React, {FormEvent, useContext, useEffect, useState} from "react";
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
import {tokenxRequestSupplier, tokenxResponseMapper} from "./support/TokenExchangeSupport";
import {
  idportenRequestSupplier,
  idportenResponseMapper,
} from "./support/IdPortenSupport";
import TokenLogin from "./TokenLogin";
import {DataContext} from "./IdportenLoginContext";

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
  clientAssertion?: Foo;
  audience?: Foo;
  subjectToken?: Foo;
  resource?: Foo;
  scope?: Foo;
  tenantId?: Foo;
  username?: Foo;
  code?: Foo;
  clientAssertionType?: Foo;
  grantType?: Foo;
  pid?: Foo;
  requestSupplier: (
    requestParameters: RequestParameters
  ) => Request | undefined;
  responseMapper: (response: Response) => Promise<string>;
}) {
  const [state, setState] = useState<State>({ type: "NOT_LOADED" });
  const [clientId, setClientId] = useState<string | undefined>(props.clientId?.default)
  const [username, setUsername] = useState<string | undefined>(props.username?.default)
  const [resource, setResource] = useState<string | undefined>(props.resource?.default)
  const [scope, setScope] = useState<string | undefined>(props.scope?.default)
  const [tenantId, setTenantId] = useState<string | undefined>(props.tenantId?.default)
  const [command, setCommand] = useState<string | undefined>(undefined)
  const [clientAssertion, setClientAssertion] = useState<string | undefined>(props.clientAssertion?.default)
  const [audience, setAudience] = useState<string | undefined>(props.audience?.default)
  const [subjectToken, setSubjectToken] = useState<string | undefined>(props.subjectToken?.default)
  const [clientAssertionType, setClientAssertionType] = useState<string | undefined>(props.clientAssertionType?.default)
  const [grantType] = useState<string | undefined>(props.grantType?.default)
  const [pid, setPid] = useState<string | undefined>(props.pid?.default)
  const {code} = useContext(DataContext)

  function getRequest() {
    return props.requestSupplier({
      clientAssertion: clientAssertion,
      audience: audience,
      subjectToken: subjectToken,
      clientId: clientId,
      resource: resource,
      scope: scope,
      tenantId: tenantId,
      username: username,
      code: code,
      clientAssertionType: clientAssertionType,
      grantType: grantType,
      pid: pid
    });
  }

  useEffect(() => {
    (async function () {
      setCommand(await generateCommand(props.requestSupplier({
        clientAssertion: clientAssertion,
        audience: audience,
        subjectToken: subjectToken,
        clientId: clientId,
        resource: resource,
        scope: scope,
        tenantId: tenantId,
        username: username,
        code: code,
        clientAssertionType: clientAssertionType,
        grantType: grantType,
        pid: pid
      })));
    })();
  }, [clientId, username, tenantId, props, resource, scope, audience, clientAssertion, clientAssertionType,
  grantType, subjectToken, pid]);

  async function generateToken(request: Request): Promise<string> {
    const response = await fetch(request);
    if (response.status / 100 !== 2) {
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
      setState({ type: "ERROR", message: "Could not generate token" });
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
        {props.clientAssertion && (
            <Form.Group className="mb-3" controlId="client_assertion">
              <Form.Label>Client Assertion</Form.Label>
              <FormControl
                  placeholder="ClientAssertion"
                  aria-label="ClientAssertion"
                  aria-describedby="generate-button"
                  value={clientAssertion}
                  onChange={(e) => setClientAssertion(e.target.value)}
              />
            </Form.Group>
        )}
        {props.subjectToken && (
            <Form.Group className="mb-3" controlId="subject_token">
              <Form.Label>Subject Token</Form.Label>
              <FormControl
                  placeholder="SubjectToken"
                  aria-label="SubjectToken"
                  aria-describedby="generate-button"
                  value={subjectToken}
                  onChange={(e) => setSubjectToken(e.target.value)}
              />
            </Form.Group>
        )}
        {props.audience && (
            <Form.Group className="mb-3" controlId="audience">
              <Form.Label>Audience</Form.Label>
              <FormControl
                  placeholder="Audience"
                  aria-label="Audience"
                  aria-describedby="generate-button"
                  value={audience}
                  onChange={(e) => setAudience(e.target.value)}
              />
            </Form.Group>
        )}
        {props.clientAssertionType && (
            <Form.Group className="mb-3" controlId="client_assertion_type">
              <Form.Label>Client Assertion Type</Form.Label>
              <FormControl
                  placeholder="ClientAssertionType"
                  aria-label="ClientAssertionType"
                  aria-describedby="generate-button"
                  value={clientAssertionType}
                  onChange={(e) => setClientAssertionType(e.target.value)}
              />
            </Form.Group>
        )}
        {props.pid && (
            <Form.Group className="mb-3" controlId="pid">
              <Form.Label>Pid</Form.Label>
              <FormControl
                  placeholder="Pid"
                  aria-label="Pid"
                  aria-describedby="generate-button"
                  value={pid}
                  onChange={(e) => setPid(e.target.value)}
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

const TokenGenerator = () => {
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
        <Col>
          <h2>Idporten</h2>
          <TokenLogin
            pid={"213"}
                />
          <TokenPanel
              code={{}}
              clientId={{ default: "889640782" }}
              clientAssertion={{default: "waew1"}}
              requestSupplier={idportenRequestSupplier}
              responseMapper={idportenResponseMapper}
          />
        </Col>

        <Col>
          <h2>Tokenx</h2>
          <TokenPanel
              clientAssertion={{ default: "889640782" }}
              subjectToken={{default: "subject"}}
              audience={{default: "aud"}}
              requestSupplier={tokenxRequestSupplier}
              responseMapper={tokenxResponseMapper}
          />
        </Col>
      </Row>
    </Container>
  );
};

export default TokenGenerator
