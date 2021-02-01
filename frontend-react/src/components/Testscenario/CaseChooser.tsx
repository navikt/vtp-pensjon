import React, {useState} from "react";
import {Button, InputGroup} from "react-bootstrap";
import {CaseScenario, TestScenario} from "./state";

interface CaseChooserProps {
    cases: CaseScenario[];
    scenario: TestScenario;
}

export default function CaseChooser(props: CaseChooserProps) {
    const {cases, scenario} = props;
    const [selectedCase, setSelectedCase] = useState<string | null>(null);
    const [showCreateCaseResult, setCreateCaseResult] = useState('');

    async function createCase(selectedCase: string) {
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
        if (response.ok) {
            let promise = await response.json();
            setCreateCaseResult(promise);
        }
        else{
            let text = await response.text();
            setCreateCaseResult(text)
        }
    }

    return (
        <div>
            <div>
                {showCreateCaseResult?.length > 0 ? <div>{showCreateCaseResult}</div> : ''}
            </div>
            <InputGroup className="mb-3">
                <select
                    className="custom-select"
                    onChange={(ev) => setSelectedCase(ev.target.value)}
                    defaultValue="opprett sak..."
                >
                    <option disabled>opprett sak...</option>
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
        </div>
    );
}
