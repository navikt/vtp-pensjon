import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {RequestResponse} from './request-response';
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class RequestResponseService {
  private baseUrl = '/data/requestResponses';

  constructor(private http: HttpClient) { }

    getRequestResponses(): Observable<RequestResponse[]> {
      return this.http.get<any>(this.baseUrl)
        .pipe(map<any, RequestResponse[]>(r => r._embedded.requestResponses))
    }
}
