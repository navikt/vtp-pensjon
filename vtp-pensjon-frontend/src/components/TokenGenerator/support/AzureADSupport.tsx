import { RequestParameters } from "../RequestParameters";

export function azureADRequestSupplier(requestParameters: RequestParameters) {
  if (requestParameters.clientId === undefined) {
    throw Error("Must supply 'clientId'");
  }
  if (requestParameters.username === undefined) {
    throw Error("Must supply 'username'");
  }
  if (requestParameters.tenantId === undefined) {
    throw Error("Must supply 'tenantId'");
  }

  if (
    requestParameters.clientId === "" ||
    requestParameters.username === "" ||
    requestParameters.tenantId === ""
  ) {
    return undefined;
  } else {
    return new Request(
      `/rest/AzureAd/${requestParameters.tenantId}/oauth2/v2.0/token`,
      {
        method: "post",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body:
          `client_id=${encodeURIComponent(requestParameters.clientId)}` +
          `&code=${encodeURIComponent(requestParameters.username)}` +
          `&redirect_uri=${encodeURIComponent("https://example.com")}` +
          `&grant_type=authorization_code`,
      }
    );
  }
}

export async function azureAdResponseMapper(
  response: Response
): Promise<string> {
  return (await response.json()).id_token;
}
