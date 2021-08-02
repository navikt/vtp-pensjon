import { RequestParameters } from "../RequestParameters";

export function maskinportenRequestSupplier(
  requestParameters: RequestParameters
) {
  if (requestParameters.clientId === undefined) {
    throw Error("Must supply 'clientId'");
  }
  if (requestParameters.scope === undefined) {
    throw Error("Must supply 'scope'");
  }

  if (requestParameters.clientId === "" || requestParameters.scope === "") {
    return undefined;
  } else {
    let exp = new Date();
    exp.setSeconds(120);

    let header = {
      alg: "none",
    };

    let payload = {
      aud: "https://maskinporten.no/",
      iss: requestParameters.clientId,
      iat: new Date().getUTCSeconds(),
      exp: exp.getUTCSeconds(),
      scope: requestParameters.scope,
      resource: requestParameters.resource,
    };

    return new Request("rest/maskinporten/token", {
      method: "post",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body:
        "grant_type=" +
        encodeURIComponent("urn:ietf:params:oauth:grant-type:jwt-bearer") +
        "&assertion=" +
        btoa(JSON.stringify(header)) +
        "." +
        btoa(JSON.stringify(payload)) +
        ".",
    });
  }
}

export async function maskinportenResponseMapper(
  response: Response
): Promise<string> {
  return (await response.json()).access_token;
}
