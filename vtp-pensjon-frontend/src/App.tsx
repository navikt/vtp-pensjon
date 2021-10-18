import * as React from "react";
import { HashRouter as Router, Route, Switch } from "react-router-dom";

import './custom.scss';

import Frame from "./components/Frame";
import Snitch from "./components/Snitch/Snitch";
import Testscenario from "./components/Testscenario/Testscenario";
import ErrorBoundary from "./components/ErrorBoundary";
import { AzureADLogin, OpenAMLogin } from "./components/AzureADLogin";
import TokenGenerator from "./components/TokenGenerator/TokenGenerator";
import Loginservice from "./components/Loginservice/Loginservice";
import {Unleash} from "./components/Unleash/unleash";
import IdPorten from "./components/IdPorten/IdPorten";

function App() {
  return (
    <Router>
      <Frame>
        <ErrorBoundary>
          <Switch>
            <Route path="/snitch">
              <Snitch />
            </Route>
            <Route
              path="/azuread/:tenant/v2.0/authorize"
              component={AzureADLogin}
            />
            <Route path="/loginservice/login" component={Loginservice} />
            <Route path="/idporten/login" component={IdPorten} />
            <Route path="/openam/authorize" component={OpenAMLogin} />
            <Route path="/tokenGenerator" component={TokenGenerator} />
            <Route path="/unleash" component={Unleash} />
            <Route exact path="/">
              <Testscenario />
            </Route>
            <Route path="*">
              <div>404 Not Found</div>
            </Route>
          </Switch>
        </ErrorBoundary>
      </Frame>
    </Router>
  );
}

export default App;
