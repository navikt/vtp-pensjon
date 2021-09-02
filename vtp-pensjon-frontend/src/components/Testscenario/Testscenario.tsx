import React, { useEffect, useReducer } from "react";
import { Container } from "react-bootstrap";
import {
  CaseScenario,
  initialState,
  reducer,
  TestScenario,
  TestScenarioTemplate,
} from "./state";
import ScenarioChooser from "./ScenarioChooser";
import ActiveScenarios from "./ActiveScenarios";

async function fetchTemplates(): Promise<TestScenarioTemplate[]> {
  const response = await fetch("/api/testscenario/templates");
  return response.json();
}

async function fetchScenarios(): Promise<TestScenario[]> {
  const response = await fetch("/api/testscenarios");
  return response.json();
}

async function fetchCases(): Promise<CaseScenario[]> {
  const response = await fetch("/api/testscenarios/cases");
  if (response.ok) return response.json();
  return [] as CaseScenario[];
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

const Testcenario = () => {
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
    fetchCases().then((cases) => dispatch({ type: "CASES_LOADED", cases }));
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
          cases={state.cases}
          onDelete={() => handleDelete(scenario.id)}
        />
      ))}
    </Container>
  );
};

export default Testcenario
