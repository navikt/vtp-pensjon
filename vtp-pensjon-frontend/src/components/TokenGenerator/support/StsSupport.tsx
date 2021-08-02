import { RequestParameters } from "../RequestParameters";

export function stsRequestSupplier(requestParameters: RequestParameters) {
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

export async function stsResponseMapper(response: Response): Promise<string> {
  return (await response.json()).access_token;
}
