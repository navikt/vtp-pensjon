import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TestScenarioTemplate} from './test-scenario-template';

@Injectable({
  providedIn: 'root'
})
export class TestScenarioTemplateService {
  private baseUrl = '/api/testscenario/templates';

  constructor(private http: HttpClient) { }

  getRequestResponses(): Observable<TestScenarioTemplate[]> {
    return this.http.get<TestScenarioTemplate[]>(this.baseUrl);
  }
}
