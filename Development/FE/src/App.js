import React from "react";
import {
  BrowserRouter as Router,
  Route,
  Redirect,
  Switch,
} from "react-router-dom";

// TODO : remove
// import Users from "./user_/pages/Users";
// import NewPlace from "./places_/pages/NewPlace";
// import UserPlaces from "./places_/pages/UserPlaces";
// import UpdatePlace from "./places_/pages/UpdatePlace";
// import Auth from "./user_/pages/Auth";
// import MainNavigation from "./shared_/components/Navigation/MainNavigation";

//

import { AuthContext } from "./shared/context/auth-context";
import { useAuth } from "./shared/hooks/auth-hook";
import MainNavigation from "./shared/components/Navigation/MainNavigation";
import Login from "./auth/pages/Login";
import RegistMain from "./user/regist/pages/RegistMain";
import RegistIndividual from "./user/regist/pages/RegistIndividual";

const App = () => {
  const { token, login, logout, userId, isAdmin } = useAuth();

  let routes;

  // if (token) {
  //   if (isAdmin) {
  //     routes = (
  //       <Switch>
  //         <Route path="/" exact>
  //           <Redirect to="/regist" />
  //         </Route>
  //         <Route path="/regist" exact>
  //           <RegistMain />
  //         </Route>
  //         <Redirect to="/" />
  //       </Switch>
  //     );
  //   } else { // 일반 대표자
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
  //   }
  // } else {
  //   routes = (
  //     <Switch>
  //       <Route path="/" exact>
  //         <Redirect to="/login" />
  //       </Route>
  //       <Route path="/login" exact>
  //         <Login />
  //       </Route>
  //       <Redirect to="/login" />
  //     </Switch>
  //   );
  // }

  return (
    <AuthContext.Provider
      value={{
        isLoggedIn: !!token,
        token: token,
        userId: userId,
        login: login,
        logout: logout,
        isAdmin: isAdmin,
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
