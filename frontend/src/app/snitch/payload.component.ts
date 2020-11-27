import {Component, Input, OnInit} from '@angular/core';
import {Payload} from "./request-response";

@Component({
  selector: 'app-payload',
  template: `
    <div class="card">
      <div class="card-header">
        {{header}}

        <div class="float-right">
          <span class="badge badge-primary">{{payload.contentType}}</span>
          &nbsp;
          <span *ngIf="payload.contentLength != null && payload.contentLength >= 1" class="badge badge-primary">{{payload.contentLength}}</span>
        </div>
      </div>
      <div class="card-body pb-0">
        <div class="card-text">
          <app-headers [headers]="payload.headers"></app-headers>
        </div>
      </div>
      <div class="card-body pt-0">
        <div class="card-text">
          <pre *ngIf="payload.contentType?.includes('application/json')">{{payload.content | atob | jsonparse | json}}</pre>
          <pre *ngIf="payload.contentType?.includes('text/xml')">{{payload.content | atob | xmlformatter }}</pre>
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
