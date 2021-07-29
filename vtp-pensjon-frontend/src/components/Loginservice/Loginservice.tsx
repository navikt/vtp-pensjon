import React, { useEffect, useState } from "react";
import { Container } from "react-bootstrap";
import { RouteComponentProps } from "react-router";

interface User {
  ident: string;
  firstName: string;
  lastName: string;
  redirect: string;
}
export default (props: RouteComponentProps<{}>) => {
  const [users, setUsers] = useState<User[]>([]);

  async function fetchUsers() {
    const response = await fetch(
      "/rest/loginservice/users" + props.location.search
    );
    const data: User[] = await response.json();
    setUsers(data);
  }

  useEffect(() => {
    fetchUsers();
    return () => {};
  }, []);

  return (
    <Container fluid>
      <h1>Logg inn som innbygger</h1>
      {users.map((user) => (
        <div key={user.ident}>
          <a href={user.redirect}>
            {user.firstName} {user.lastName} ({user.ident})
          </a>
        </div>
      ))}
    </Container>
  );
};
