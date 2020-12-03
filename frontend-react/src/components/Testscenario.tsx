import React, { useEffect, useState } from 'react';
import {Container, InputGroup, Form, Button} from "react-bootstrap";

export interface TestScenarioTemplate {
    key: string
    navn: string
}

async function fetchScenarios(): Promise<TestScenarioTemplate[]> {
    const response = await fetch('/api/testscenario/templates');
    return response.json();
}

async function createScenario(scenario: string): Promise<void> {
    const response = await fetch('/api/testscenarios/' + scenario, { method: 'post' });
    return response.json();
}

interface ScenarioChooserProps {
    scenarios: TestScenarioTemplate[]
    onCreate: (key: string) => void
}
function ScenarioChooser(props: ScenarioChooserProps) {
    const { scenarios, onCreate } = props;
    const [selectedScenario, setSelectedScenario] = useState<string | null>(null);
    return (
        <InputGroup className="mb-3">
            <InputGroup.Prepend>
                <InputGroup.Text>
                    Template
                </InputGroup.Text>
            </InputGroup.Prepend>
            <Form.Control as="select" onChange={ev => setSelectedScenario(ev.target.value)}>
                <option disabled>...</option>
                {scenarios.map(s => <option key={s.key} value={s.key}>{s.navn}</option>)}
            </Form.Control>
            <InputGroup.Append>
                <Button variant="outline-secondary" disabled={selectedScenario == null} onClick={() => selectedScenario != null && onCreate(selectedScenario)}>
                    Create
                </Button>
            </InputGroup.Append>
        </InputGroup>
    );
}

export default () => {
    const [scenarios, setScenarios] = useState<TestScenarioTemplate[]>([]);


    useEffect(() => {
        fetchScenarios().then(data => setScenarios(data));
        return () => {}
    }, []);

    return (
        <Container  style={{ paddingTop: '12px' }}>
            <ScenarioChooser scenarios={scenarios} onCreate={createScenario} />
        </Container>
    );
}
