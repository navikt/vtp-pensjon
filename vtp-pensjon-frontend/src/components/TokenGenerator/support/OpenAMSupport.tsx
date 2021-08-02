import { RequestParameters } from "../RequestParameters";

export function openAMRequestSupplier(requestParameters: RequestParameters) {
  if (requestParameters.clientId === undefined) {
    throw Error("Must supply 'clientId'");
  }
  if (requestParameters.username === undefined) {
    throw Error("Must supply 'username'");
  }

  if (requestParameters.clientId === "" || requestParameters.username === "") {
    return undefined;
  } else {
    return new Request(
      `/rest/isso/oauth2/access_token` +
        `?grant_type=authorization_code` +
        `&code=${encodeURI(requestParameters.username)};`,
      {
        method: "POST",
        headers: {
          Authorization: "Basic " + btoa(`${requestParameters.clientId}:dummy`),
        },
      }
    );
  }
}

export async function openAmResponseMapper(
  response: Response
): Promise<string> {
  return (await response.json()).id_token;
}
