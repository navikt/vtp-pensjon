import { CaseScenario, TestScenario } from "./state";
import { Button, Card, Container } from "react-bootstrap";
import React from "react";
import ScenarioChooser from "./ScenarioChooser";
import CaseChooser from "./CaseChooser";

interface ActiveScenarioProps {
  scenario: TestScenario;
  cases: CaseScenario[];
  onDelete: () => any;
}

export default function ActiveScenarios(props: ActiveScenarioProps) {
  const { scenario, cases, onDelete } = props;
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
                scenario.personopplysninger?.soekerIdent
              }
            >
              {scenario.personopplysninger?.soekerIdent}
            </a>
          </li>
        </ul>
        {cases === undefined || cases.length == 0 ? (
          ""
        ) : (
          <ul>
            <li>
              <CaseChooser
                key={scenario.id}
                cases={cases}
                scenario={scenario}
              />
            </li>
          </ul>
        )}
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
