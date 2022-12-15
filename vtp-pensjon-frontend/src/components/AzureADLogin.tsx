import * as React from "react";
import { useEffect, useState } from "react";
import { RouteComponentProps } from "react-router";
import { Button, Card, Col, Container, Row } from "react-bootstrap";

interface NAVAnsatt {
  cn: string;
  givenname: string;
  sn: string;
  displayName: string;
  email: string;
  groups: string[];
  enheter: number[];
}

interface User {
  username: string;
  displayName: string;
  redirect: string;
  details: NAVAnsatt;
}

async function fetchUsers(
  usersUrl: string,
  queryParams: string
): Promise<User[]> {
  // /rest/AzureAd/sdfgsdfg/v2.0/users?client_id=sdfgsdfg&state=dfgh&nonce=asd&redirect_uri=sdfg
  const response = await fetch(`${usersUrl}${queryParams}`);
  if (response.status === 200) {
    return response.json();
  } else {
    const data = await response.json();
    throw new Error(`HTTP ${response.status} ${data.message || data.error}`);
  }
}

type State =
  | {
      type: "LOADING";
    }
  | {
      type: "LOADED";
      users: User[];
    }
  | {
      type: "ERROR";
      error: Error;
    };

function imageUrl(): string {
  return "/assets/saksbehandler.svg";
}

const Login: React.FC<{ usersUrl: string; queryParams: string }> = (props) => {
  const [state, setState] = useState<State>({ type: "LOADING" });
  useEffect(() => {
    fetchUsers(props.usersUrl, props.queryParams)
      .then((users) => {
        users.sort((a, b) => a.displayName.localeCompare(b.displayName));

        users.forEach((user) => {
          user.details.groups.sort();
          user.details.enheter.sort();
        });

        setState({
          type: "LOADED",
          users: users,
        });
      })
      .catch((error) =>
        setState({
          type: "ERROR",
          error,
        })
      );

    return () => {};
  }, [props.usersUrl, props.queryParams]);

  if (state.type === "LOADING") {
    return <div>Loading...</div>;
  } else if (state.type === "ERROR") {
    return <div>Error from VTP backend API: {state.error.message}</div>;
  } else {
    return (
      <Container fluid>
        <h1>Velg bruker</h1>
        {state.users.map((user) => (
          <div key={user.username}>
            <Card
              style={{
                padding: "12px",
                marginBottom: "12px",
              }}
            >
              <Row>
                <Col sm={1}>
                  <img
                    className="img-fluid"
                    src={imageUrl()}
                    alt="Bruker"
                    style={{
                      maxHeight: "80px",
                      maxWidth: "100px",
                    }}
                  />
                </Col>
                <Col sm={4}>
                  <Card.Title>{user.displayName}</Card.Title>
                  <Card.Body>
                    <dl className="row">
                      <dt className="col-sm-3">Brukernavn</dt>
                      <dd className="col-sm-9">{user.details.cn}</dd>
                      <dt className="col-sm-3">Fornavn</dt>
                      <dd className="col-sm-9">{user.details.givenname}</dd>
                      <dt className="col-sm-3">Etternavn</dt>
                      <dd className="col-sm-9">{user.details.sn}</dd>
                      <dt className="col-sm-3">E-post</dt>
                      <dd className="col-sm-9">{user.details.email}</dd>
                    </dl>
                  </Card.Body>
                </Col>
                <Col sm={5}>
                  <Card.Title>Tilgangsgrupper</Card.Title>
                  <Card.Body>
                    <ul className="list-unstyled">
                      {user.details.groups.map((group) => (
                        <li key={user.username + group}>{group}</li>
                      ))}
                    </ul>
                  </Card.Body>
                </Col>
                <Col sm={2}>
                  <Card.Title>Enheter</Card.Title>
                  <Card.Body>
                    <ul className="list-unstyled">
                      {user.details.enheter.map((enhet) => (
                        <li key={user.username + enhet}>{enhet}</li>
                      ))}
                    </ul>
                  </Card.Body>
                </Col>
              </Row>
              <Row>
                <Col className="text-center">
                  <Button href={user.redirect} variant={"primary"}>
                    Logg inn som {user.displayName}
                  </Button>
                </Col>
              </Row>
            </Card>
          </div>
        ))}
      </Container>
    );
  }
};

export function AzureADLogin(props: RouteComponentProps<{ tenant: string }>) {
  const tenant = props.match.params.tenant;
  const queryParams = props.location.search;
  const usersUrl = `/rest/AzureAd/${tenant}/v2.0/users`;
  return <Login usersUrl={usersUrl} queryParams={queryParams} />;
}
