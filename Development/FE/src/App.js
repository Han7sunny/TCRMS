import React from "react";
import {
  BrowserRouter as Router,
  Route,
  Redirect,
  Switch,
} from "react-router-dom";

// TODO : remove
// import Users from "./user_/pages/Users";
import NewPlace from "./places_/pages/NewPlace";
import UserPlaces from "./places_/pages/UserPlaces";
import UpdatePlace from "./places_/pages/UpdatePlace";
import Auth from "./user_/pages/Auth";
// import MainNavigation from "./shared_/components/Navigation/MainNavigation";
import { AuthContext } from "./shared_/context/auth-context";
import { useAuth } from "./shared_/hooks/auth-hook";
//

import Login from "./auth/pages/Login";
import MainNavigation from "./shared/components/Navigation/MainNavigation";

const App = () => {
  const { token, login, logout, userId } = useAuth();

  let routes;

  if (token) {
    routes = (
      <Switch>
        <Route path="/" exact>
          <Login />
        </Route>
        <Route path="/:userId/places" exact>
          <UserPlaces />
        </Route>
        <Route path="/places/new" exact>
          <NewPlace />
        </Route>
        <Route path="/places/:placeId">
          <UpdatePlace />
        </Route>
        <Redirect to="/" />
      </Switch>
    );
  } else {
    routes = (
      <Switch>
        <Route path="/" exact>
          <Login />
        </Route>
        <Route path="/:userId/places" exact>
          <UserPlaces />
        </Route>
        <Route path="/auth">
          <Auth />
        </Route>
        <Redirect to="/auth" />
      </Switch>
    );
  }

  return (
    <AuthContext.Provider
      value={{
        isLoggedIn: !!token,
        token: token,
        userId: userId,
        login: login,
        logout: logout,
      }}
    >
      <Router>
        {!!token && <MainNavigation />}
        <main>{routes}</main>
      </Router>
    </AuthContext.Provider>
  );
};

export default App;
