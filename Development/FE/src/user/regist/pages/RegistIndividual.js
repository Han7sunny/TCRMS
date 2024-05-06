import React, { useState } from "react";

import {
  TABLE_COLUMNS_REGIST_INDIVIDUAL,
  TABLE_COLUMNS_CHECK_INDIVIDUAL,
} from "../../../shared/util/regist-columns";
import { useRegist } from "../../../shared/hooks/regist-hook";
import { useHttpClient } from "../../../shared/hooks/http-hook";

import RegistTable from "../components/RegistTable";
import Button from "../../../shared/components/TableInputElements/Button";

import "./RegistIndividual.css";

const RegistIndividual = () => {
  const [isFirst, setIsFirst] = useState(true);
  const [isRegistMode, setIsRegistMode] = useState(false);
  const { isLoading, error, sendRequest, clearError } = useHttpClient();

  // focus disabled  colinfo,control,     data,initialValue
  const [registState, inputHandler, addRow, deleteRow, setRegistData] =
    useRegist(
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

  // 맨처음에 데이터 가져와서 세팅
  // Data submit : submit 전에 체크로직 확인

  // const authSubmitHandler = async (event) => {
  //   event.preventDefault();

  //   if (!isFirst) {
  //     try {
  //       // const responseData = await sendRequest(
  //       //   `${process.env.REACT_APP_BACKEND_URL}/api/login`,
  //       //   "POST",
  //       //   JSON.stringify({
  //       //     uniName: formState.inputs.uniname.value,
  //       //     userName: formState.inputs.username.value,
  //       //     userPass: formState.inputs.password.value,
  //       //   }),
  //       //   {
  //       //     "Content-Type": "application/json",
  //       //   }
  //       // );

  //       // TODO : change Dummy DATA
  //       const responseData = {
  //         is_first_login: true,
  //         userId: 1,
  //         token: "asdf",
  //         isAdmin: false,
  //       };

  //       if (responseData.is_first_login) {
  //         setIsFirst(responseData.is_first_login);
  //         this.forceUpdate();
  //       } else {
  //         auth.login(
  //           responseData.userId,
  //           responseData.token,
  //           responseData.isAdmin
  //         );
  //       }
  //     } catch (err) {}
  //   } else {
  //     try {
  //       // const formData = new FormData();
  //       // formData.append("email", formState.inputs.uniName.value);
  //       // formData.append("name", formState.inputs.userName.value);
  //       // formData.append("password", formState.inputs.password.value);
  //       // const responseData = await sendRequest(
  //       //   `${process.env.REACT_APP_BACKEND_URL}/api/changePW`,
  //       //   "POST",
  //       //   formData
  //       // );

  //       // TODO : change Dummy DATA
  //       const responseData = {
  //         userId: 1,
  //         token: "asdf",
  //         isAdmin: false,
  //       };

  //       //비밀번호 변경 후 로그인
  //       auth.login(
  //         responseData.userId,
  //         responseData.token,
  //         responseData.isAdmin
  //       );
  //     } catch (err) {}
  //   }
  // };

  const switchModeHandler = () => {
    setIsRegistMode(true);
  };

  return (
    <div className="regist-event">
      <h1 className="regist-event-title">
        {isRegistMode ? "개인전 신청" : "개인전 신청확인"}
      </h1>
      {isRegistMode ? (
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
          <div className="regist-btn-submit">
            <Button type="submit">{isFirst ? "신청하기" : "수정완료"}</Button>
          </div>
        </form>
      ) : (
        <div className="regist-form">
          <RegistTable
            columns={TABLE_COLUMNS_CHECK_INDIVIDUAL}
            data={registState.inputs}
            showNumber
          />
          <div className="check-btn-submit">
            <Button onClick={switchModeHandler}>추가 및 수정하기</Button>
          </div>
        </div>
      )}
    </div>
  );
};

export default RegistIndividual;
