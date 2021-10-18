import {Badge, Button, Card, Dropdown, Table} from "react-bootstrap";
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
    setSelectedRequest: (request: RequestResponse) => any;
    onClear: () => void,
    onIgnorePath: (path: string) => void
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
                    <th>Path</th>
                    <th>Status</th>
                    <th className="d-none d-xxl-table-cell">Handler</th>
                    <th/>
                </tr>
                </thead>
                <tbody>
                {props.requests.map((request, i) => {
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
