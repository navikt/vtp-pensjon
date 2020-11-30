import * as React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import 'bootstrap/dist/css/bootstrap.min.css';

import Frame from "./components/Frame";
import Snitch from "./components/Snitch";
import Testscenario from "./components/Testscenario";
import ErrorBoundary from "./components/ErrorBoundary";

function App() {
    return (
        <Router>
            <Frame>
                <ErrorBoundary>
                    <Switch>
                        <Route path="/snitch">
                            <Snitch />
                        </Route>
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

ReactDOM.render(<App />, document.getElementById(('app')));
