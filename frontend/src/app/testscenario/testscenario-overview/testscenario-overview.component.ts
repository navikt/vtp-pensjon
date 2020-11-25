import { Component, OnInit } from '@angular/core';
import {TestScenarioTemplateService} from '../test-scenario-template.service';
import {TestScenarioTemplate} from '../test-scenario-template';
import {TestScenarioService} from '../test-scenario.service';
import {TestScenario} from '../test-scenario';

@Component({
  selector: 'app-testscenario-overview',
  templateUrl: './testscenario-overview.component.html',
  styleUrls: ['./testscenario-overview.component.css']
})
export class TestscenarioOverviewComponent implements OnInit {
  testScenarioTemplates: TestScenarioTemplate[] = [];
  testScenarios: TestScenario[] = [];
  selectedTemplate?: TestScenarioTemplate;

  constructor(private testScenarioTemplateService: TestScenarioTemplateService, private testScenarioService: TestScenarioService) { }

  ngOnInit(): void {
    this.testScenarioTemplateService.getRequestResponses()
      .subscribe(it => {
        this.testScenarioTemplates = it;
        if (it.length >= 0) {
          this.selectedTemplate = it[0];
        }
      });

    this.fetchTestScenarios();
  }

  private fetchTestScenarios(): void {
    this.testScenarioService.getRequestResponses()
      .subscribe(it => this.testScenarios = it);
  }

  soekerIdent(personopplysninger?: any): string | null {
    if (personopplysninger != null) {
      return personopplysninger.søkerIdent;
    } else {
      return null;
    }
  }

  onCreateTemplate(selectedTemplate?: TestScenarioTemplate): void {
    if (selectedTemplate != null) {
      this.testScenarioService.createFromTemplate(selectedTemplate)
        .subscribe(it => this.testScenarios.unshift(it));
    }
  }

  deleteTestScenario(testScenario: TestScenario): void {
    this.testScenarioService.delete(testScenario)
      .subscribe(_ => this.fetchTestScenarios());
  }
}
