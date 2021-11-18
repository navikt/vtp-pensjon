import {Form, FormControl} from "react-bootstrap";
import React, {useCallback, useContext, useEffect, useState} from "react";
import {DataContext} from "./IdportenLoginContext";

const TokenLogin = (props: any) => {

    const [pid, setPid] = useState<string>()
    const {setCode} = useContext(DataContext)
    const updateContext = useCallback(function(res){
        setCode(res)
    }, [setCode]);

    useEffect(() => {
        console.log("Idporten Login")
        fetch(`http://localhost:8060/rest/idporten/login-helper?pid=${pid}&state=123&nonce=1231`, {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                        'mode': 'cors',
                    },
                }).then(res => res.text().then(res => {
                    updateContext(res)
                }))
    }, [pid, updateContext])

    return(
        <Form style={{ marginBottom: "12px" }}>
        <Form.Group className="mb-3" controlId="Pid">
            <Form.Label>Pid</Form.Label>
            <FormControl
                placeholder="Pid"
                aria-label="Pid"
                aria-describedby="generate-button"
                value={pid}
                onChange={(e) => setPid(e.target.value)}
            />
        </Form.Group>
        </Form>
    )
}

export default TokenLogin