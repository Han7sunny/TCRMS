import React from "react";

import Button from "../../../shared/components/FormElements/Button";

import "./RegistMain.css";

const RegistMain = () => {
  return (
    <div className="regist-main">
      <Button to="/regist/individual" exact inverse>
        개인전 신청
      </Button>
      <Button to="/regist/team" exact inverse>
        단체전 신청
      </Button>
      <Button to="/regist/second" exact inverse>
        세컨 신청
      </Button>
      <Button to="/regist/volunteer" exact inverse>
        자원봉사자 신청
      </Button>
    </div>
  );
};

export default RegistMain;
