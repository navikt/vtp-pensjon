import {Component, Input, OnInit} from '@angular/core';
import {RequestResponse} from "./request-response";

@Component({
  selector: 'app-summary',
  template: `
    <div class="card" >
      <div class="card-header">
        {{requestResponse.path}}
        <div class="float-right">
          <span class="badge badge-primary">{{requestResponse.method}}</span>
          &nbsp;
          <span class="badge" [ngClass]="badgeColor(requestResponse.status)">{{requestResponse.status}}</span>
        </div>
      </div>
      <table class="table table-responsive table-borderless table-sm mb-0 p-3">
        <tr>
          <th>Timestamp</th>
          <td>{{requestResponse.timestamp}}</td>
        </tr>
        <tr>
          <th>URL</th>
          <td>{{requestResponse.url}}</td>
        </tr>
        <tr *ngIf="requestResponse.handler">
          <th>Handler</th>
          <td>{{requestResponse.handler}}</td>
        </tr>
        <tr>
          <th *ngIf="requestResponse.exception">Exception</th>
          <td *ngIf="requestResponse.exception">{{requestResponse.exception}}</td>
        </tr>
      </table>
    </div>
  `,
  styles: ['']
})
export class SummaryComponent {
  @Input() requestResponse!: RequestResponse;

  badgeColor(status: number) {
    if (status >= 200 && status <= 299) {
      return 'badge-success';
    } else if (status >= 400 && status <= 499) {
      return 'badge-warning';
    } else if (status >= 500 && status <= 599) {
      return 'badge-danger';
    } else {
      return 'badge-primary';
    }
  }
}
