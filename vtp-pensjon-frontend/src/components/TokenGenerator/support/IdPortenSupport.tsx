import { RequestParameters } from "../RequestParameters";

export function idportenRequestSupplier(
    requestParameters: RequestParameters
) {
    if (requestParameters.clientId === undefined) {
        throw Error("Must supply 'clientId'");
    }
    if (requestParameters.code === undefined) {
        throw Error("Must supply 'code'");
    }
    if (requestParameters.clientAssertion === undefined) {
        throw Error("Must supply 'clientAssertion'");
    }
    if (requestParameters.clientAssertionType === undefined) {
        throw Error("Must supply 'clientAssertionType'");
    }

    if (requestParameters.clientId === "" || requestParameters.code === "" ||
        requestParameters.clientAssertion === "" || requestParameters.clientAssertionType === "") {
        return undefined;
    } else {

        let exp = new Date();
        exp.setSeconds(120);

        return new Request("rest/idporten/token", {
            method: "post",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body:
                `grant_type=authorization_code&` +
                `client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer&` +
                `client_assertion=${encodeURIComponent(requestParameters.clientAssertion)}&` +
                `code=${encodeURIComponent(requestParameters.code)}&` +
                `client_id=${encodeURIComponent(requestParameters.clientId)}`,
        });
    }
}

export async function idportenResponseMapper(
    response: Response
): Promise<string> {
    return (await response.json()).access_token;
}
