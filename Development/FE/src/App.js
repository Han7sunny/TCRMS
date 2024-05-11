import React from "react";
import {
  BrowserRouter as Router,
  Route,
  Redirect,
  Switch,
} from "react-router-dom";

import { AuthContext } from "./shared/context/auth-context";
import { HttpContext } from "./shared/context/http-context";
import { useAuth } from "./shared/hooks/auth-hook";
import { useHttpClient } from "./shared/hooks/http-hook";

import ErrorModal from "./shared/components/UIElements/ErrorModal";
import LoadingSpinner from "./shared/components/UIElements/LoadingSpinner";
import MainNavigation from "./shared/components/Navigation/MainNavigation";
import Login from "./auth/pages/Login";
import RegistMain from "./user/regist/pages/RegistMain";
import RegistIndividual from "./user/regist/pages/RegistIndividual";

const App = () => {
  const { token, login, logout, userId, isAdmin, isFirstLogin } = useAuth();
  const { isLoading, error, sendRequest, setError, clearError } =
    useHttpClient();

  let routes;
  let isLogin = !!token && !isFirstLogin;

  if (isLogin) {
    if (isAdmin) {
      routes = (
        <Switch>
          <Route path="/" exact>
            <Redirect to="/regist" />
          </Route>
          <Route path="/regist" exact>
            <RegistMain />
          </Route>
          <Redirect to="/" />
        </Switch>
      );
    } else {
      // 일반 대표자
      routes = (
        <Switch>
          <Route path="/" exact>
            <Redirect to="/regist" />
          </Route>
          <Route path="/regist" exact>
            <RegistMain />
          </Route>
          <Route path="/regist/individual" exact>
            <RegistIndividual />
          </Route>
          <Route path="/docu" exact>
            {/* <NewPlace /> */}
          </Route>
          <Route path="/submit" exact>
            {/* <NewPlace /> */}
          </Route>
          <Redirect to="/" />
        </Switch>
      );
    }
  } else {
    routes = (
      <Switch>
        <Route path="/" exact>
          <Redirect to="/login" />
        </Route>
        <Route path="/login" exact>
          <Login />
        </Route>
        <Redirect to="/login" />
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
        isAdmin: isAdmin,
        isFirstLogin: isFirstLogin,
      }}
    >
      <HttpContext.Provider
        value={{
          isLoading,
          error,
          sendRequest,
          setError,
          clearError,
        }}
      >
        <ErrorModal
          title={error.title}
          error={error.detail}
          onClear={clearError}
        />
        {isLoading && <LoadingSpinner asOverlay />}
        <Router>
          {isLogin && <MainNavigation />}
          <main>{routes}</main>
        </Router>
      </HttpContext.Provider>
    </AuthContext.Provider>
  );
};

export default App;
