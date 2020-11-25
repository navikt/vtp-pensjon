import {Component, Input, OnInit} from '@angular/core';
import {RequestResponse} from "./request-response";

@Component({
  selector: 'app-summary',
  template: `
    <div class="card" >
      <div class="card-header">Summary</div>
      <table class="table table-responsive table-borderless table-sm mb-0 p-3">
        <tr>
          <th>Timestamp</th>
          <td>{{requestResponse.timestamp}}</td>
        </tr>
        <tr>
          <th>Method</th>
          <td>{{requestResponse.method}}</td>
        </tr>
        <tr>
          <th>Path</th>
          <td>{{requestResponse.path}}</td>
        </tr>
        <tr>
          <th>Status</th>
          <td>{{requestResponse.status}}</td>
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
}
