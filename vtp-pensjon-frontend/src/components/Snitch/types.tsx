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
  queryString: String | null;
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

export class RequestFilters {

  constructor(paths: string[], methods: string[], status: string[]) {
    this.paths = paths
    this.methods = methods
    this.status = status
  }

  paths: string[] = []
  methods: string[] = []
  status: string[] = []
}
