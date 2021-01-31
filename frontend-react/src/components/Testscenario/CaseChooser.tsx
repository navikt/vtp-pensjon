import React, { useState } from "react";
import {Button, InputGroup} from "react-bootstrap";
import { CaseScenario, TestScenario } from "./state";

interface CaseChooserProps {
  cases: CaseScenario[];
  scenario: TestScenario;
}


export default function CaseChooser(props: CaseChooserProps) {
  const {cases, scenario } = props;
  const [selectedCase, setSelectedCase] = useState<string | null>(null);
  const [showCreateCaseResult, setCreateCaseResult] = useState<string | ''>('');

  async function createCase(selectedCase: string) {
      console.log("called onCreate case! " + selectedCase + " scenario id " + scenario.id);
      let response = await fetch("/api/testscenarios/cases", {
          method: 'POST',
          headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json',
          },
          body: JSON.stringify({
              testScenarioId: scenario.id,
              caseId: selectedCase
          })
      });
      let promise = await response.json();
      setCreateCaseResult(promise);
      return promise;
  }

  return (
    <InputGroup className="mb-3">
        <InputGroup.Append>
            {showCreateCaseResult?.length > 0 ? <div>{showCreateCaseResult}</div> : ''}
        </InputGroup.Append>
      <select
        className="custom-select"
        onChange={(ev) => setSelectedCase(ev.target.value)}
        defaultValue="opprett sak"
      >
          <option disabled>opprett sak</option>
        {cases.map((caseScenario) => (
          <option key={caseScenario.id} value={caseScenario.id}>
            {caseScenario.navn}
          </option>
        ))}
      </select>
      <InputGroup.Append>
        <Button
          variant="outline-secondary"
          disabled={selectedCase == null}
          onClick={() => selectedCase != null && createCase(selectedCase)}
        >
          Create
        </Button>
      </InputGroup.Append>
    </InputGroup>
  );
}
