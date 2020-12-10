import * as React from "react";
import { RouteComponentProps } from "react-router";
import { useEffect, useState } from "react";
import { Card, Container } from "react-bootstrap";

interface User {
  username: string;
  displayName: string;
  redirect: string;
}

async function fetchUsers(
  usersUrl: string,
  queryParams: string
): Promise<User[]> {
  // /rest/AzureAd/sdfgsdfg/v2.0/users?client_id=sdfgsdfg&state=dfgh&nonce=asd&redirect_uri=sdfg
  const response = await fetch(`${usersUrl}${queryParams}`);
  if (response.status === 200) {
    return await response.json();
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

function imageUrl(user: string): string {
  switch (user) {
    case "darthvad":
      return "/assets/darthvad.jpg";
    case "prinleia":
      return "/assets/prinleia.jpg";
    case "lukesky":
      return "/assets/lukesky.png";
    default:
      return "/assets/saksbehandler.svg";
  }
}

const Login: React.FC<{ usersUrl: string; queryParams: string }> = (props) => {
  const [state, setState] = useState<State>({ type: "LOADING" });
  useEffect(() => {
    fetchUsers(props.usersUrl, props.queryParams)
      .then((users) =>
        setState({
          type: "LOADED",
          users: users,
        })
      )
      .catch((error) =>
        setState({
          type: "ERROR",
          error,
        })
      );

    return () => {};
  }, []);

  if (state.type === "LOADING") {
    return <div>Loading...</div>;
  } else if (state.type === "ERROR") {
    return <div>Error from VTP backend API: {state.error.message}</div>;
  } else {
    return (
      <Container>
        <h1>Velg bruker</h1>
        {state.users.map((user) => (
          <a href={user.redirect} key={user.username}>
            <Card
              className={"mb-2"}
              style={{
                flexDirection: "row",
                alignItems: "center",
                padding: "2px",
              }}
            >
              <div style={{ width: "100px" }}>
                <img
                  className="img-fluid"
                  src={imageUrl(user.username)}
                  alt="Bruker"
                  style={{ maxHeight: "80px", maxWidth: "100px" }}
                />
              </div>
              <div>
                <h3 style={{ marginLeft: "10px" }}>{user.displayName}</h3>
              </div>
            </Card>
          </a>
        ))}
      </Container>
    );
  }
};

export function OpenAMLogin(props: RouteComponentProps<{}>) {
  return (
    <Login
      usersUrl="/rest/isso/oauth2/users"
      queryParams={props.location.search}
    />
  );
}
export function AzureADLogin(props: RouteComponentProps<{ tenant: string }>) {
  const tenant = props.match.params.tenant;
  const queryParams = props.location.search;
  const usersUrl = `/rest/AzureAd/${tenant}/v2.0/users`;
  return <Login usersUrl={usersUrl} queryParams={queryParams} />;
}
