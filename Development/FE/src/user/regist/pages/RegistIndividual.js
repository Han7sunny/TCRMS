import React, { useState } from "react";

import {
  TABLE_COLUMNS_REGIST_INDIVIDUAL,
  TABLE_COLUMNS_CHECK_INDIVIDUAL,
} from "../../../shared/util/regist-columns";
import { EVENT_ID, WEIGHT_ID } from "../../../shared/util/const-event";
import { useRegist } from "../../../shared/hooks/regist-hook";
import { HttpContext } from "../../shared/context/http-context";
import { AuthContext } from "../../shared/context/auth-context";

import RegistTable from "../components/RegistTable";
import Button from "../../../shared/components/TableInputElements/Button";

import "./RegistIndividual.css";

const RegistIndividual = () => {
  const auth = useContext(AuthContext);
  const http = useContext(HttpContext);

  const [isFirst, setIsFirst] = useState(true);
  const [isRegistMode, setIsRegistMode] = useState(false);

  // focus disabled  colinfo,control,     data,initialValue
  const [registState, inputHandler, addRow, deleteRow, setRegistData] =
    useRegist(
      [
        // {
        //   name: "홍길동",
        //   sex: "남성",
        //   foreigner: ["외국인"],
        //   nationality: "영국",
        //   idnumber: ["200101", "-", "2000000"],
        //   event: ["겨루기"],
        //   weight: "핀",
        // },
        {
          name: "홍길동",
          sex: "남성",
          foreigner: ["외국인"],
          nationality: "영국",
          idnumber: "200101-2000000",
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

  const formatParticipant = (participant, mode) => {
    if (mode === 1) {
      let event = new Set();
      if (participant.event) {
        // 배열이라 length로 판단해야하나
        participant.event.forEach((eventId) => {
          let eventName = Object.keys(EVENT_ID).find(
            (key) => EVENT_ID[key] === eventId
          );
          event.add(eventName.split(" ")[2]);
        });
      }
      event = [...event];

      let weight = "";
      if (participant.weightClassId) {
        if (participant.gender === "남성") {
          weight = Object.keys(WEIGHT_ID["남성"]).find(
            (key) => WEIGHT_ID["남성"][key] === participant.weightClassId
          );
        } else if (participant.gender === "여성") {
          weight = Object.keys(WEIGHT_ID["여성"]).find(
            (key) => WEIGHT_ID["여성"][key] === participant.weightClassId
          );
        }
      }

      return {
        participantId: participant.participantId,
        name: participant.name,
        sex: participant.gender, // 남성,여성인지 체크
        foreigner: participant.isForeigner ? ["외국인"] : [],
        nationality: participant.nationality,
        idnumber: participant.identityNumber
          ? [
              participant.identityNumber.substr(0, 6),
              "-",
              participant.identityNumber.substr(8, 14),
            ]
          : [],
        event: event,
        weight: weight,
      };
    }

    if (mode === 2) {
      let identityNumber = participant.idnumber.join("");
      if (identityNumber === "-") identityNumber = undefined;

      let eventId = [];
      participant.event.forEach((eventname) => {
        let eventKey = "개인전 " + participant.sex + " " + eventname;
        eventId.push(EVENT_ID[eventKey]);
      });

      let weightClassId;
      if (participant.sex === "남성")
        weightClassId = WEIGHT_ID["남성"][participant.weight];
      else weightClassId = WEIGHT_ID["여성"][participant.weight];

      return {
        participantId: participant.participantId,
        name: participant.name,
        gender: participant.sex,
        isForeigner: participant.foreigner.length > 0 ? true : false,
        nationality: participant.nationality,
        identityNumber: identityNumber,
        eventId: eventId,
        weightClassId: weightClassId,
      };
    }
  };

  // 개인전 페이지 들어오면 먼저 개인전 저장된 데이터 있는지 체크
  const individualListHandler = async (event) => {
    event.preventDefault();

    try {
    // const responseData = await http.sendRequest(
    //   `${process.env.REACT_APP_BACKEND_URL}/api/user/individual-list`,
    //   headers={
    //     Authorization: `Bearer ${auth.token}`
    //   },
    //   errorModalTitle="개인전 선수 로드 실패"
    // );

    // TODO : change Dummy DATA
    const responseData = {
      isSuccess: true,
      payload: {isParticipantExists: true, participants: [{
        participantId: 1,
        weightClassId: 1,
        name: "조서영",
        identityNumber: "961201-0000000",
        gender: "여성",
        isForeigner: false,
        nationality: "",
        eventId: [1, 2],
      },
      {
        participantId: 2,
        //weightClassId: ,
        name: "조땡땡",
        identityNumber: "961201-0000000",
        gender: "남성",
        isForeigner: true,
        nationality: "영국",
        eventId: [4],
      }]},
    };
    // const responseData = {
    //   isSuccess: true,
    //   payload: {isParticipantExists: false}
    // };

    if (responseData.payload.isParticipantExists) {
      setIsRegistMode(false);
      setRegistData(responseData.payload.participants.map(participant => formatParticipant(participant, 1)));
    } else {
      setIsRegistMode(true); //useRegist 초기값 정하기
    }

  } catch (err) {
  }};

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

  const switchModeHandler = (event) => {
    event.preventDefault();

    if (isRegistMode) { // api post
      setRegistData([
        {
          name: "홍길동",
          sex: "남성",
          foreigner: ["외국인"],
          nationality: "영국",
          idnumber: "200101-2000000",
          event: ["겨루기"],
          weight: "핀",
        },
      ]);
    } else { // 기존 데이터 그대로 
      setRegistData([
        {
          name: "홍길동",
          sex: "남성",
          foreigner: ["외국인"],
          nationality: "영국",
          idnumber: ["200101", "-", "2000000"],
          event: ["겨루기"],
          weight: "핀",
        },
      ]);
    }
    setIsRegistMode(!isRegistMode);
  };

  return (
    <div className="regist-event">
      <h2 className="regist-event-title">
        {isRegistMode ? "개인전 신청" : "개인전 신청확인"}
      </h2>
      {isRegistMode ? (
        <form className="regist-form" onSubmit={switchModeHandler}>
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
