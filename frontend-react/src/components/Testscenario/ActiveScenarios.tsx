import { TestScenario } from "./state";
import { Button, Card } from "react-bootstrap";
import React from "react";

interface ActiveScenarioProps {
  scenario: TestScenario;
  onDelete: () => any;
}

export default function ActiveScenarios(props: ActiveScenarioProps) {
  const { scenario, onDelete } = props;
  return (
    <Card>
      <Card.Header>
        {scenario.templateNavn}{" "}
        <Button
          size="sm"
          variant="danger"
          className="float-right"
          onClick={onDelete}
        >
          Delete
        </Button>
      </Card.Header>
      <Card.Body>
        <Card.Title>Åpne i PSAK</Card.Title>
        <ul>
          <li>
            <a
              href={
                "http://localhost:9080/psak/brukeroversikt/fnr=" +
                scenario.personopplysninger?.søkerIdent
              }
            >
              {scenario.personopplysninger?.søkerIdent}
            </a>
          </li>
        </ul>
      </Card.Body>
      <Card.Body>
        <Card.Title>Body</Card.Title>
        <div style={{ overflowY: "scroll", height: "500px" }}>
          <pre>{JSON.stringify(scenario, null, "  ")}</pre>
        </div>
      </Card.Body>
    </Card>
  );
}
