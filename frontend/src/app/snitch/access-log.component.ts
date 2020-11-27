import {Component, Input, Output, EventEmitter} from '@angular/core';
import {RequestResponse} from "./request-response";

@Component({
  selector: 'app-access-log',
  template: `
    <table class="table table-sm table-striped table-hover">
      <thead>
      <tr>
        <th>Timestamp</th>
        <th>Method</th>
        <th>Path</th>
        <th>Status</th>
      </tr>
      </thead>
      <tbody>
      <tr *ngFor="let requestResponse of requestResponses" (click)="select(requestResponse)" [ngClass]="rowClass(requestResponse)">
        <td>{{requestResponse.timestamp | date:'HH:mm:ss.SSS'}}</td>
        <td>{{requestResponse.method}}</td>
        <td>{{requestResponse.path}}</td>
        <td>{{requestResponse.status}}</td>
      </tr>
      </tbody>
    </table>
`,
  styles: ['']
})
export class AccessLogComponent {
  @Input() requestResponses: RequestResponse[] = [];
  @Output() selectRequestResponse = new EventEmitter<RequestResponse>();

  @Input() selectedRequestResponse?: RequestResponse;

  select(requestResponse: RequestResponse) {
    this.selectedRequestResponse = requestResponse;
    this.selectRequestResponse.emit(requestResponse);
  }

  rowClass(requestResponses: RequestResponse): string {
    if (requestResponses === this.selectedRequestResponse) {
      return 'table-primary'
    } else if (requestResponses.status >= 400 && requestResponses.status <= 499) {
      return 'table-warning'
    } else if (requestResponses.status >= 500 && requestResponses.status <= 500) {
      return 'table-danger'
    } else {
      return ''
    }
  }
}
