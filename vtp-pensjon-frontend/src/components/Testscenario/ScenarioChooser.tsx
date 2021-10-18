import { TestScenarioTemplate } from "./state";
import React, { useState } from "react";
import {Button, InputGroup} from "react-bootstrap";

interface ScenarioChooserProps {
  templates: TestScenarioTemplate[];
  onCreate: (key: string) => void;
}

export default function ScenarioChooser(props: ScenarioChooserProps) {
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
        <Button
          variant="outline-secondary"
          disabled={selectedScenario == null}
          onClick={() => selectedScenario != null && onCreate(selectedScenario)}
        >
          Create
        </Button>
    </InputGroup>
  );
}
