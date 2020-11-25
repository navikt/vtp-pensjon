import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {RequestResponse} from './request-response';

@Injectable({
  providedIn: 'root'
})
export class RequestResponseService {
  private baseUrl = '/api/snitch';

  constructor(private http: HttpClient) { }

    getRequestResponses(): Observable<RequestResponse[]> {
      return this.http.get<RequestResponse[]>(this.baseUrl);
    }
}
