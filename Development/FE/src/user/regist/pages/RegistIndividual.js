import React from "react";

import "./RegistIndividual.css";
import RegistIndividualTable from "../components/RegistIndividualTable";

const RegistIndividual = () => {
  // 테이블 길이 늘어나면 사이드바 고정 되는지 확인하기
  return (
    <div className="regist-event">
      <h1 className="regist-event-title">개인전 신청</h1>
      <RegistIndividualTable />
    </div>
  );
};

export default RegistIndividual;
