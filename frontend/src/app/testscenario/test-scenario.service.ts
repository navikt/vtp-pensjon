import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TestScenario} from './test-scenario';
import {TestScenarioTemplate} from './test-scenario-template';

@Injectable({
  providedIn: 'root'
})
export class TestScenarioService {
  private baseUrl = '/api/testscenarios';

  constructor(private http: HttpClient) { }

  getRequestResponses(): Observable<TestScenario[]> {
    return this.http.get<TestScenario[]>(this.baseUrl);
  }

  createFromTemplate(template: TestScenarioTemplate): Observable<TestScenario> {
    return this.http.post<TestScenario>(this.baseUrl + '/' + template.key, null);
  }

  delete(testScenario: TestScenario): Observable<void> {
    return this.http.delete<void>(this.baseUrl + '/' + testScenario.id);
  }
}
