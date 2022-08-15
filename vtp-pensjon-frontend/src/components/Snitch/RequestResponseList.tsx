import {Badge, Button, Card, Dropdown, Table, Form} from "react-bootstrap";
import React from "react";
import {RequestFilters, RequestResponse} from "./types";
import FormattedDate from "./FormattedDate";
import {badgeBackground} from "./Snitch";
import {Gear, Trash} from "react-bootstrap-icons";
import RequestResponseSummary from "./RequestResponseSummary";

type CustomToggleProps = {
    children?: React.ReactNode;
    onClick: (event: React.MouseEvent<HTMLAnchorElement, MouseEvent>) => {};
};

const CustomToggle = React.forwardRef(
    (props: CustomToggleProps, ref: React.Ref<HTMLAnchorElement>) => (
        <a
            href={"#foo"}
            ref={ref}
            onClick={e => {
                e.preventDefault();
                props.onClick(e);
            }}
        >
            {props.children}
            <Gear/>
        </a>
    )
);

function handleFilter(filters: string[], candidate: string): string[] {
    if (filters.indexOf(candidate) !== -1) {
        return filters.filter((value) => {
            return value !== candidate
        })
    }
    return filters.concat(candidate)
}

export default function RequestResponseList(props: {
    requests: RequestResponse[];
    selectedRequest: RequestResponse | null;
    setSelectedRequest: (request: RequestResponse | null) => any;
    onClear: () => void;
    onIgnorePath: (path: string) => void;
    filters: RequestFilters;
    setFilters: (filters: RequestFilters) => any;
}) {
    return (
        <Card>
            <Card.Header>
                <Card.Title>
                    Access log
                    <div className="float-end">
                        <Button size={"sm"} variant="primary" onClick={props.onClear}>Clear <Trash/></Button>
                    </div>
                </Card.Title>

            </Card.Header>

            <Table striped hover size="sm" responsive={"md"} className={"mb-0"}>
                <thead>
                <tr>
                    <th>Timestamp</th>
                    <th>
                    <Dropdown className="mx-2" autoClose="outside">
                        <Dropdown.Toggle variant="secondary">Method</Dropdown.Toggle>
                        <Dropdown.Menu>
                            {props.requests
                                .filter((request, i) => {
                                    return props.requests.findIndex((value, i) => { return value.method === request.method }) === i
                                })
                                .map((request, i) => {
                                return <Form className="mx-2">
                                    <Form.Check
                                        type={"checkbox"}
                                        label={request.method}
                                        onChange={() => {
                                            props.setFilters(
                                                new RequestFilters(
                                                    props.filters.paths,
                                                    handleFilter(props.filters.methods, request.method),
                                                    props.filters.status
                                                )
                                            )
                                        }}
                                    />
                                </Form>
                            })}
                        </Dropdown.Menu>
                    </Dropdown>
                    </th>
                    <th>
                    <Dropdown className="mx-2" autoClose="outside">
                        <Dropdown.Toggle variant="secondary">Path</Dropdown.Toggle>
                        <Dropdown.Menu>
                            {props.requests
                                .filter((request, i) => {
                                    return props.requests.findIndex((value, i) => { return value.path === request.path }) === i
                                })
                                .map((request, i) => {
                                return <Form className="mx-2">
                                    <Form.Check
                                        type={"checkbox"}
                                        label={request.path}
                                        onChange={() => {
                                            props.setFilters(
                                                new RequestFilters(
                                                    handleFilter(props.filters.paths, request.path),
                                                    props.filters.methods,
                                                    props.filters.status
                                                )
                                            )
                                        }}
                                    />
                                </Form>
                            })}
                        </Dropdown.Menu>
                    </Dropdown>
                    </th>
                    <th>
                    <Dropdown className="mx-2" autoClose="outside">
                        <Dropdown.Toggle variant="secondary">Status</Dropdown.Toggle>
                        <Dropdown.Menu>
                            {props.requests
                                .filter((request, i) => {
                                    return props.requests.findIndex((value, i) => { return value.status.toString() === request.status.toString() }) === i
                                })
                                .map((request, i) => {
                                return <Form className="mx-2">
                                    <Form.Check
                                        type={"checkbox"}
                                        label={request.status}
                                        onChange={() => {
                                            props.setFilters(
                                                new RequestFilters(
                                                    props.filters.paths,
                                                    props.filters.methods,
                                                    handleFilter(props.filters.status, request.status.toString())
                                                )
                                            )
                                        }}
                                    />
                                </Form>
                            })}
                        </Dropdown.Menu>
                    </Dropdown>
                    </th>
                    <th className="d-none d-xxl-table-cell">Handler</th>
                    <th/>
                </tr>
                </thead>
                <tbody>
                {props.requests.filter( request => {
                    let pathCheck = true
                    let methodCheck = true
                    let statusCheck = true

                    if (props.filters.paths.length > 0) {
                        pathCheck = props.filters.paths.includes(request.path);
                    }

                    if (props.filters.methods.length > 0) {
                        methodCheck = props.filters.methods.includes(request.method)
                    }

                    if (props.filters.status.length > 0) {
                        statusCheck = props.filters.status.includes(request.status.toString())
                    }

                    return pathCheck && methodCheck && statusCheck
                }).map((request, i) => {
                    let rowClass =
                        request === props.selectedRequest ? "table-primary" : "";

                    let details;
                    if (request === props.selectedRequest) {
                        details = (<><tr className={"pt-0 pb-0"}>
                            {
                            }
                            <td colSpan={6} className={"pt-0 pb-0"}  style={{wordWrap:"break-word", wordBreak:"break-all"}}>
                                <RequestResponseSummary request={props.selectedRequest}/>
                            </td>
                        </tr>
                        </>)
                    } else (
                        details = (<React.Fragment key={(request.id || i)}/>)
                    )

                    return (
                        <React.Fragment key={(request.id || i) + "details2"}>
                            <tr
                                className={rowClass}
                                style={{cursor: "pointer"}}
                                onClick={() => props.setSelectedRequest(request)}
                            >
                                <td>
                                    <FormattedDate time={request.timestamp}/>
                                </td>
                                <td>{request.method}</td>
                                <td style={{  wordWrap:"break-word", wordBreak:"break-all"}}>{request.path}</td>
                                <td>
                                    <Badge pill={true} bg={badgeBackground(request.status)}>
                                        {request.status}
                                    </Badge>
                                </td>
                                <td className="d-none d-xxl-table-cell">{request.handlerClass}</td>
                                <td>
                                    <Dropdown>
                                        <Dropdown.Toggle as={CustomToggle} id="dropdown-custom-components">
                                        </Dropdown.Toggle>

                                        <Dropdown.Menu>
                                            <Dropdown.Item onClick={() => props.onIgnorePath(request.path)}>Ignore Path</Dropdown.Item>
                                        </Dropdown.Menu>
                                    </Dropdown>
                                </td>
                            </tr>
                            {details}
                        </React.Fragment>
                    );
                })}
                    </tbody>
                    </Table>
                    </Card>
                    );
                }
