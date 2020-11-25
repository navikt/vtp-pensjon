import {Component, Input, OnInit} from '@angular/core';
import {Payload} from "./request-response";

@Component({
  selector: 'app-payload',
  template: `
    <div class="card">
      <div class="card-header">{{header}}</div>
      <div class="card-body">
        <h5 class="card-title">Headers</h5>
        <div class="card-text">
          <app-headers [headers]="payload.headers"></app-headers>
        </div>
      </div>
      <div class="card-body">
        <h5 class="card-title">Content</h5>
        <div class="card-text">
          <table class="table table-sm table-borderless">
            <tr>
              <th>Content Type</th>
              <td>{{payload.contentType}}</td>
            </tr>
            <tr>
              <th>Content Length</th>
              <td>{{payload.contentLength}}</td>
            </tr>
          </table>
        </div>
      </div>
      <div class="card-body">
        <h5 class="card-title">Body</h5>
        <div class="card-text">
          <pre *ngIf="payload.contentType === 'application/json'">{{payload.content | atob | jsonparse | json}}</pre>
        </div>
      </div>
    </div>
  `,
  styles: ['']
})
export class PayloadComponent {
  @Input() payload!: Payload
  @Input() header!: string
}
