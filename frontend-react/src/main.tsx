import * as React from "react";
import ReactDOM from "react-dom";
import { HashRouter as Router, Switch, Route } from "react-router-dom";

import "bootstrap/dist/css/bootstrap.min.css";

import Frame from "./components/Frame";
import Snitch from "./components/Snitch/Snitch";
import Testscenario from "./components/Testscenario";
import ErrorBoundary from "./components/ErrorBoundary";
import { AzureADLogin, OpenAMLogin } from "./components/AzureADLogin";

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
            <Route path="/openam/authorize" component={OpenAMLogin} />
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
