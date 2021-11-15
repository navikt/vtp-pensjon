import { RequestParameters } from "../RequestParameters";

export function tokenxRequestSupplier(requestParameters: RequestParameters) {
    if (requestParameters.clientAssertion === undefined) {
        throw Error("Must supply 'clientAssertion'");
    }
    if (requestParameters.audience === undefined) {
        throw Error("Must supply 'audience'");
    }
    if (requestParameters.subjectToken === undefined) {
        throw Error("Must supply 'subjectToken'");
    }

    if (
        requestParameters.clientAssertion === "" ||
        requestParameters.audience === "" ||
        requestParameters.subjectToken === ""
    ) {
        return undefined;
    } else {
        return new Request(
            `/rest/tokenx/token`,
            {
                method: "post",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                },
                body:
                    `grant_type=urn:ietf:params:oauth:grant-type:token-exchange&` +
                    `client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer&` +
                    `client_assertion=${encodeURIComponent(requestParameters.clientAssertion)}&` +
                    `subject_token_type=urn:ietf:params:oauth:token-type:jwt&` +
                    `subject_token=${encodeURIComponent(requestParameters.subjectToken)}&` +
                    `audience=${encodeURIComponent(requestParameters.audience)}`,
            }
        );
    }
}

export async function tokenxResponseMapper(
    response: Response
): Promise<string> {
    return ((await response.json()).access_token)
}
