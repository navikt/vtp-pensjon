import { Component, OnInit } from '@angular/core';
import {RequestResponseService} from './request-response.service';
import {RequestResponse} from './request-response';

@Component({
  selector: 'app-snitch',
  templateUrl: './snitch.component.html',
  styleUrls: ['./snitch.component.css']
})
export class SnitchComponent implements OnInit {
  requestResponses: RequestResponse[] = [];
  selectedRequestResponse: RequestResponse | undefined;

  constructor(private requestResponseService: RequestResponseService) { }

  ngOnInit(): void {
    this.requestResponseService.getRequestResponses()
      .subscribe(requestResponses => {
        this.requestResponses = requestResponses.sort((r1, r2) => r1.timestamp > r2.timestamp ? -1 : 1);
        if (this.requestResponses.length > 0) {
          this.selectedRequestResponse = this.requestResponses[0];
        }
      });
  }

  onSelect(requestResponse: RequestResponse): void {
    this.selectedRequestResponse = requestResponse;
  }
}
