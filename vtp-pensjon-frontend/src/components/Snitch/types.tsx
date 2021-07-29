export interface Payload {
  headers: Map<string, string[]>;
  contentType: string | null;
  contentLength: number | null;
  content: string | null;
}

export interface RequestResponse {
  id: string;
  timestamp: string;
  path: string;
  url: string;
  method: string;
  handlerClass: string;
  handlerMethod: string;
  status: number;
  handler: string | null;
  exception: string | null;
  stackTrace: string | null;

  request: Payload;

  response: Payload;
}
