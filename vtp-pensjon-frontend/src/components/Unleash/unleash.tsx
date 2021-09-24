import React, {useCallback, useEffect, useState} from "react";
import {Button, Container, Form, Modal, Table} from "react-bootstrap";
import BootstrapSwitchButton from 'bootstrap-switch-button-react'

interface FeatureResponse {
    version: number,
    features: Feature[]
}

interface Feature {
    name: string,
    description: string,
    enabled: boolean,
    strategies: Strategy[] | undefined,
    variants: string | undefined,
    createdAt: string | undefined
}

interface Strategy {
    name: string
}

function NewFeature(name: string, enabled: boolean) : Feature {
    return {
        name: name,
        description: "",
        enabled: enabled,
        createdAt: undefined,
        strategies: undefined,
        variants: undefined,
    }
}

function AddFeatureForm(props: { name: string, enabled: boolean, onNameChange: (name: string) => void, onEnabledChange: (enabled: boolean) => void}) {
    return <Form>
        <Form.Group className="mb-3" controlId="formBasicName">
            <Form.Label>Name</Form.Label>
            <Form.Control type="string" placeholder="Name" value={props.name} onChange={(event) => { props.onNameChange(event.target.value) } }/>
            <Form.Text className="text-muted">
                Name of feature as used in code (e.g. <code>pesys.psak.PL-12345.my-awesome-new-feature</code>)
            </Form.Text>
        </Form.Group>

        <Form.Group className="mb-3" controlId="formBasicCheckbox">
            <Form.Label>Enabled</Form.Label><br/>
            <BootstrapSwitchButton
                checked={props.enabled} size="sm" onlabel={"yes"} offlabel={"no"}
                onChange={props.onEnabledChange}
            />
        </Form.Group>
    </Form>
}

function AddFeatureModal(props: { show: boolean, onClose: () => void, onAddFeature: (feature: Feature) => void,
    name: string, enabled: boolean, onNameChange: (name: string) => void, onEnabledChange: (enabled: boolean) => void
}) {
    return <Modal
        show={props.show}
        onHide={props.onClose}
    >
        <Modal.Header closeButton>
            <Modal.Title>Add feature</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <AddFeatureForm name={props.name} enabled={props.enabled} onNameChange={props.onNameChange} onEnabledChange={props.onEnabledChange}/>
        </Modal.Body>
        <Modal.Footer>
            <Button variant="secondary" onClick={props.onClose}>
                Cancel
            </Button>
            <Button variant="primary" onClick={() => props.onAddFeature(NewFeature(props.name, props.enabled))}>Add feature</Button>
        </Modal.Footer>
    </Modal>
}

function Features(props: { features: Feature[], onToggleFeature: (feature: Feature, checked: boolean) => void, onDeleteFeature: (feature: Feature) => void }) {
    return <>
        <Table>
            <thead>
            <tr>
                <th>Name</th>
                <th>Enabled</th>
                <th>Delete</th>
            </tr>
            </thead>
            <tbody>
            {props.features.map((feature: Feature) => (
                <tr key={feature.name}>
                    <td>{feature.name}</td>
                    <td>
                        <BootstrapSwitchButton
                            checked={feature.enabled} size="sm" onlabel={"yes"} offlabel={"no"}
                            onChange={(checked) => {
                                props.onToggleFeature(feature, checked)
                            }}
                        />
                    </td>
                    <td><Button size={"sm"} onClick={() => {
                        props.onDeleteFeature(feature)
                    }}>Delete</Button></td>
                </tr>
            ))}
            </tbody>
        </Table>
    </>
}

export function Unleash() {
    const [features, setFeatures] = useState<Feature[]>([])
    const [showAddFeature, setShowAddFeature] = useState<boolean>(false)

    const [name, setName] = useState<string>("")
    const [enabled, setEnabled] = useState<boolean>(true)

    const fetchFeatures = useCallback(async () => {
        const result = await fetch("/rest/unleash/api/client/features");
        const data = await result.json() as FeatureResponse

        setFeatures(data.features)
    }, [])

    function showNewFeature() {
        setName("")
        setEnabled(true)
        setShowAddFeature(true)
    }

    function addFeature(feature: Feature) {
        fetch(
            "/rest/unleash/api/client/features",
            {
                method: "put",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(feature)
            }
        ).then(() => {
            return fetchFeatures()
        })
    }

    function toggleFeature(feature: Feature, checked: boolean) {
        feature.enabled = checked

        fetch(
            "/rest/unleash/api/client/features",
            {
                method: "put",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(feature)
            }
        ).then(() => {
            return fetchFeatures()
        })
    }

    function deleteFeature(feature: Feature) {
        fetch(
            "/rest/unleash/api/client/features/" + encodeURI(feature.name),
            {
                method: "delete"
            }
        ).then(() => {
            return fetchFeatures()
        })
    }

    useEffect(() => {
        fetchFeatures()
    }, [fetchFeatures]);

    return <Container style={{paddingTop: "12px"}}>
        <h1>Features
                <Button className={"float-right"} variant={"success"}
                        onClick={showNewFeature}>
                    Add feature
                </Button>
        </h1>
        <Features features={features} onToggleFeature={toggleFeature} onDeleteFeature={deleteFeature}/>
        <AddFeatureModal show={showAddFeature} onClose={() => setShowAddFeature(false)} onAddFeature={(feature) => {
            addFeature(feature)
            setShowAddFeature(false)
        }} enabled={enabled} onEnabledChange={setEnabled} name={name} onNameChange={setName}/>
    </Container>
}
