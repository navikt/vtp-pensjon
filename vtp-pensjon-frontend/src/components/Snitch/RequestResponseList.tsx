import {Badge, Button, Card, Dropdown, Table, Form} from "react-bootstrap";
import React from "react";
import {RequestResponse} from "./types";
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

export default function RequestResponseList(props: {
    requests: RequestResponse[];
    selectedRequest: RequestResponse | null;
    setSelectedRequest: (request: RequestResponse | null) => any;
    onClear: () => void;
    onIgnorePath: (path: string) => void;
    filters: string[];
    setFilters: (filters: string[]) => any;
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
                    <th>Method</th>
                    <Dropdown className="mx-2" autoClose="outside">
                        <Dropdown.Toggle id="dropdown-autoclose-outside">
                            Path
                        </Dropdown.Toggle>
                        <Dropdown.Menu>
                            {props.requests.map((request, i) => {
                                return <Form className="mx-2">
                                    <Form.Check
                                        type={"checkbox"}
                                        label={request.path}
                                        onChange={() => {
                                            console.log("Before: " + props.filters)
                                            if (props.filters.indexOf(request.path) !== -1) {
                                                console.log("Set filters: " + props.filters.filter((path) => { return path !== request.path }))
                                                props.setFilters(
                                                    props.filters.filter((path) => { return path !== request.path })
                                                )
                                            } else {
                                                console.log("Set filters (pop): " + props.filters)
                                                props.setFilters(
                                                    props.filters.concat(request.path)
                                                );
                                            }
                                        }}
                                    />
                                </Form>
                            })}
                        </Dropdown.Menu>
                    </Dropdown>
                    <th>Status</th>
                    <th className="d-none d-xxl-table-cell">Handler</th>
                    <th/>
                </tr>
                </thead>
                <tbody>
                {props.requests.filter( request => {
                    if (props.filters.length === 0) {
                        console.log("Re-draw: Empty filter!")
                        return true;
                    } else {
                        console.log(request.path + " in " + props.filters + "? -> " + props.filters.includes(request.path))
                        return props.filters.includes(request.path);
                    }
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
