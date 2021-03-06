import * as React from "react";
import { Link } from "react-router-dom";
import {
  Navbar,
  Nav,
  Button,
  FormControl,
  Form,
  NavDropdown,
} from "react-bootstrap";

interface Props {
  children?: React.ReactNode;
}
export default (props: Props) => (
  <>
    <Navbar variant="dark" bg="dark" expand="lg">
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
