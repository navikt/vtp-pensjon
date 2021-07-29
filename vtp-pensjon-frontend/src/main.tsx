import * as React from "react";
import ReactDOM from "react-dom";
import { HashRouter as Router, Route, Switch } from "react-router-dom";

import "bootstrap/dist/css/bootstrap.min.css";

import Frame from "./components/Frame";
import Snitch from "./components/Snitch/Snitch";
import Testscenario from "./components/Testscenario/Testscenario";
import ErrorBoundary from "./components/ErrorBoundary";
import { AzureADLogin, OpenAMLogin } from "./components/AzureADLogin";
import TokenGenerator from "./components/TokenGenerator/TokenGenerator";
import Loginservice from "./components/Loginservice/Loginservice";

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
            <Route path="/openam/authorize" component={OpenAMLogin} />
            <Route path="/tokenGenerator" component={TokenGenerator} />
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

ReactDOM.render(<App />, document.getElementById("app"));
