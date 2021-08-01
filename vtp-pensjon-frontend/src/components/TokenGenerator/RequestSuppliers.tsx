export interface RequestParameters {
  username: string | undefined;
  clientId: string | undefined;
  tenantId: string | undefined;
}

export function generateStsRequest(requestParameters: RequestParameters) {
  if (requestParameters.username === "") {
    return undefined;
  } else {
    return new Request("/rest/v1/sts/token?grant_type=dfg&scope=fghfgh", {
      method: "GET",
      headers: {
        Authorization: "Basic " + btoa(`${requestParameters.username}:dummy`),
      },
    });
  }
}

export async function stsResponeMapper(response: Response): Promise<string> {
  return (await response.json()).access_token;
}

export function generateOpenAMRequest(requestParameters: RequestParameters) {
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

export function generateAzureADRequest(requestParameters: RequestParameters) {
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
