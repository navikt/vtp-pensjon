import React, { useEffect, useReducer, useState } from "react";
import { Button, Card, Container, InputGroup } from "react-bootstrap";

interface TestScenarioTemplate {
  key: string;
  navn: string;
}

interface TestScenario {
  id: string;
  templateKey?: string;
  templateNavn?: string;
  testscenarioId?: string;
  personopplysninger?: any;
}

interface TestScenarioState {
  templatesLoading: boolean;
  templates: TestScenarioTemplate[];
  scenariosLoading: boolean;
  scenarios: TestScenario[];
}

type TestscenarioAction =
  | {
      type: "TEMPLATES_LOADING";
    }
  | {
      type: "TEMPLATES_LOADED";
      templates: TestScenarioTemplate[];
    }
  | {
      type: "SCENARIOS_LOADING";
    }
  | {
      type: "SCENARIOS_LOADED";
      scenarios: TestScenario[];
    };

const initialState: TestScenarioState = {
  templatesLoading: false,
  templates: [],
  scenariosLoading: false,
  scenarios: [],
};

function reducer(
  state: TestScenarioState,
  action: TestscenarioAction
): TestScenarioState {
  switch (action.type) {
    case "TEMPLATES_LOADING":
      return {
        ...state,
        templatesLoading: true,
      };
    case "TEMPLATES_LOADED":
      return {
        ...state,
        templatesLoading: false,
        templates: action.templates,
      };
    case "SCENARIOS_LOADING":
      return {
        ...state,
        scenariosLoading: true,
      };
    case "SCENARIOS_LOADED":
      return {
        ...state,
        scenariosLoading: false,
        scenarios: action.scenarios,
      };
    default:
      return state;
  }
}

async function fetchTemplates(): Promise<TestScenarioTemplate[]> {
  const response = await fetch("/api/testscenario/templates");
  return response.json();
}

async function fetchScenarios(): Promise<TestScenario[]> {
  const response = await fetch("/api/testscenarios");
  return response.json();
}

async function createScenario(templateId: string): Promise<void> {
  const response = await fetch("/api/testscenarios/" + templateId, {
    method: "post",
  });
  return response.json();
}

async function deleteScenario(scenarioId: string): Promise<void> {
  await fetch("/api/testscenarios/" + scenarioId, { method: "delete" });
}

interface ScenarioChooserProps {
  templates: TestScenarioTemplate[];
  onCreate: (key: string) => void;
}

function ScenarioChooser(props: ScenarioChooserProps) {
  const { templates, onCreate } = props;
  const [selectedScenario, setSelectedScenario] = useState<string | null>(null);
  return (
    <InputGroup className="mb-3">
      <select
        className="custom-select"
        onChange={(ev) => setSelectedScenario(ev.target.value)}
        defaultValue="..."
      >
        <option disabled>...</option>
        {templates.map((tmpl) => (
          <option key={tmpl.key} value={tmpl.key}>
            {tmpl.navn}
          </option>
        ))}
      </select>
      <InputGroup.Append>
        <Button
          variant="outline-secondary"
          disabled={selectedScenario == null}
          onClick={() => selectedScenario != null && onCreate(selectedScenario)}
        >
          Create
        </Button>
      </InputGroup.Append>
    </InputGroup>
  );
}

interface ActiveScenarioProps {
  scenario: TestScenario;
  onDelete: () => any;
}
function ActiveScenarios(props: ActiveScenarioProps) {
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
                "http://localhost:9080/psak/brukeroversikf/fnr=" +
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

export default () => {
  const [state, dispatch] = useReducer(reducer, initialState);

  async function handleCreateScenario(templateKey: string) {
    await createScenario(templateKey);
    const scenarios = await fetchScenarios();
    dispatch({ type: "SCENARIOS_LOADED", scenarios });
  }

  async function handleDelete(id: string) {
    await deleteScenario(id);
    const scenarios = await fetchScenarios();
    dispatch({ type: "SCENARIOS_LOADED", scenarios });
  }

  useEffect(() => {
    dispatch({ type: "TEMPLATES_LOADING" });
    fetchTemplates().then((templates) =>
      dispatch({ type: "TEMPLATES_LOADED", templates })
    );
    fetchScenarios().then((scenarios) =>
      dispatch({ type: "SCENARIOS_LOADED", scenarios })
    );
    return () => {};
  }, []);

  return (
    <Container style={{ paddingTop: "12px" }}>
      <ScenarioChooser
        templates={state.templates}
        onCreate={handleCreateScenario}
      />
      {state.scenarios.map((scenario) => (
        <ActiveScenarios
          key={scenario.id}
          scenario={scenario}
          onDelete={() => handleDelete(scenario.id)}
        />
      ))}
    </Container>
  );
};
