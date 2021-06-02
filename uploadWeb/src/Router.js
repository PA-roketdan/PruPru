import React from "react";
import { HashRouter as Router, Route, Switch } from "react-router-dom";
import DBWrite from "./DBWrite";

const AppRouter = () => {
  return (
    <Router>
      <Switch>
        <>
          <Route exact path="/">
            <DBWrite />
          </Route>
        </>
      </Switch>
    </Router>
  );
};

export default AppRouter;
