import React from "react";

import NavLinks from "./NavLinks";
import SideDrawer from "./SideDrawer";
import "./MainNavigation.css";

const MainNavigation = () => {
  return (
    <React.Fragment>
      <SideDrawer show>
        <nav className="main-navigation__drawer-nav">
          <NavLinks />
        </nav>
      </SideDrawer>
    </React.Fragment>
  );
};

export default MainNavigation;
