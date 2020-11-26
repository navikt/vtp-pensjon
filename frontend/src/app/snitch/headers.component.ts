import {Component, Input, OnInit} from '@angular/core';
import {Payload} from "./request-response";

@Component({
  selector: 'app-headers',
  template: `
    <table class="table table-sm table-borderless">
      <tr *ngFor="let header of headers | keyvalue">
        <th style="width: 200px">{{header.key}}</th>
        <td>{{header.value}}</td>
      </tr>
    </table>
  `,
  styles: ['']
})
export class HeadersComponent {
  @Input() headers!: Map<string, string[]>
}
