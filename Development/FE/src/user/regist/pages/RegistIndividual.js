import React from "react";

import { TABLE_COLUMNS_REGIST_INDIVIDUAL } from "../../../shared/util/regist-columns";
import { useRegist } from "../../../shared/hooks/regist-hook";

import RegistTable from "../components/RegistTable";
import Button from "../../../shared/components/TableInputElements/Button";

import "./RegistIndividual.css";

const RegistIndividual = () => {
  // focus disabled  colinfo,control,     data,initialValue
  const [registState, inputHandler, addRow, deleteRow] = useRegist(
    [
      {
        name: "홍길동",
        sex: "남성",
        foreigner: ["외국인"],
        nationality: "영국",
        idnumber: ["200101", "-", "2000000"],
        event: ["겨루기"],
        weight: "핀",
      },
    ],
    {
      name: "",
      sex: "",
      foreigner: [],
      nationality: "",
      idnumber: ["", "-", ""],
      event: [],
      weight: "",
    }
  );

  const addRowHandler = (event) => {
    event.preventDefault();
    addRow();
  };

  const deleteRowHandler = (event) => {
    event.preventDefault();
    const rowNum = Number(event.target.id.split("-")[0].replace("row", ""));
    deleteRow(rowNum);
  };

  // submit 전에 체크로직 확인

  // 테이블 길이 늘어나면 사이드바 고정 되는지 확인하기
  return (
    <div className="regist-event">
      <h1 className="regist-event-title">개인전 신청</h1>
      <form className="regist-form">
        <div className="regist-btn-add-row">
          <Button onClick={addRowHandler}>선수 추가</Button>
        </div>
        <RegistTable
          columns={TABLE_COLUMNS_REGIST_INDIVIDUAL}
          data={registState.inputs}
          inputHandler={inputHandler}
          buttonHandler={deleteRowHandler}
          showNumber
        />
        {/* <Button type="submit" disabled={!formState.isValid}> */}
        <div className="regist-btn-submit">
          <Button type="submit">신청하기</Button>
        </div>
      </form>
    </div>
  );
};

export default RegistIndividual;
