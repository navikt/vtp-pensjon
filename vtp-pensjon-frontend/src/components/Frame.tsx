import * as React from "react";
import { Link } from "react-router-dom";
import {
  Navbar,
  Nav,
} from "react-bootstrap";

interface Props {
  children?: React.ReactNode;
}

const Frame = (props: Props) => (
  <>
    <Navbar variant="dark" bg="dark" expand="sm">
      <Navbar.Brand href="/">VTP Pensjon</Navbar.Brand>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse>
        <Nav className="mr-auto">
          <Link className="nav-link" to="/">
            Datascenarioer
          </Link>
          <Link className="nav-link" to="/snitch">
            Snitch
          </Link>
          <Link className="nav-link" to="/unleash">
            Unleash
          </Link>
          <Link className="nav-link" to="/tokenGenerator">
            Generer token
          </Link>
          <a className="nav-link" href="http://localhost:8060/swagger-ui/">
            Swagger
          </a>
        </Nav>
      </Navbar.Collapse>
    </Navbar>
    {props.children}
  </>
);

export default Frame
