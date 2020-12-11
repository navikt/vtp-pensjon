export interface TestScenarioTemplate {
  key: string;
  navn: string;
}

export interface TestScenario {
  id: string;
  templateKey?: string;
  templateNavn?: string;
  testscenarioId?: string;
  personopplysninger?: any;
}

export interface TestScenarioState {
  templatesLoading: boolean;
  templates: TestScenarioTemplate[];
  scenariosLoading: boolean;
  scenarios: TestScenario[];
}

export type TestscenarioAction =
  | {
      type: "TEMPLATES_LOADING";
    }
  | {
      type: "TEMPLATES_LOADED";
      templates: TestScenarioTemplate[];
    }
  | {
      type: "SCENARIOS_LOADING";
    }
  | {
      type: "SCENARIOS_LOADED";
      scenarios: TestScenario[];
    };

export const initialState: TestScenarioState = {
  templatesLoading: false,
  templates: [],
  scenariosLoading: false,
  scenarios: [],
};

export function reducer(
  state: TestScenarioState,
  action: TestscenarioAction
): TestScenarioState {
  switch (action.type) {
    case "TEMPLATES_LOADING":
      return {
        ...state,
        templatesLoading: true,
      };
    case "TEMPLATES_LOADED":
      return {
        ...state,
        templatesLoading: false,
        templates: action.templates,
      };
    case "SCENARIOS_LOADING":
      return {
        ...state,
        scenariosLoading: true,
      };
    case "SCENARIOS_LOADED":
      return {
        ...state,
        scenariosLoading: false,
        scenarios: action.scenarios,
      };
    default:
      return state;
  }
}
