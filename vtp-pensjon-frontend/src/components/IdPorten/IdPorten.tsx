import React, { useEffect, useState } from "react";
import { Container } from "react-bootstrap";
import { RouteComponentProps } from "react-router";

interface User {
  ident: string;
  firstName: string;
  lastName: string;
  redirect: string;
}

const IdPorten = (props: RouteComponentProps) => {
  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    const fetchUsers = async function () {
      const response = await fetch(
          "/rest/idporten/users" + props.location.search
      );
      const data: User[] = await response.json();
      setUsers(data);
    }

    fetchUsers();
    return () => {};
  }, [props.location.search]);

  return (
    <Container fluid>
      <h1>ID-Porten Mock</h1>
      <h2>Logg inn som innbygger</h2>
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

export default IdPorten
