export async function generateCommand(
  request: Request | undefined
): Promise<string | undefined> {
  if (request !== undefined) {
    let method = `--request ${request.method.toUpperCase()}`;
    let url = `'${request.url}'`;

    let headers = "";
    request.headers.forEach((value: string, key: string) => {
      headers += `--header '${key}: ${value}'`;
    });

    let data;
    if (request.body) {
      data = `--data '${await request.text()}'`;
    } else {
      data = "";
    }

    let curl = `curl ${method} ${url} ${headers} ${data}`.trim();
    let jq = `jq`;

    return `${curl} | ${jq}`;
  } else {
    return undefined;
  }
}
