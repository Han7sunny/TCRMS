import React, { useState, useContext, useEffect, useCallback } from "react";

import {
  TABLE_COLUMNS_REGIST_VOLUNTEER,
  TABLE_COLUMNS_CHECK_VOLUNTEER,
} from "../../../shared/util/regist-columns";
import { EVENT_ID } from "../../../shared/util/const-event";
import { checkValidityVolunteer } from "../../../shared/util/regist-validators";
import { useRegist } from "../../../shared/hooks/regist-hook";
import { HttpContext } from "../../../shared/context/http-context";
import { AuthContext } from "../../../shared/context/auth-context";

import RegistTable from "../components/RegistTable";
import Button from "../../../shared/components/TableInputElements/Button";

import "./RegistIndividual.css";

const RegistVolunteer = () => {
  const auth = useContext(AuthContext);
  const { sendRequest, setError } = useContext(HttpContext);

  const [isFirst, setIsFirst] = useState(true);
  const [isRegistMode, setIsRegistMode] = useState(false);
  const [apiFail, setApiFail] = useState(false);

  const [registState, inputHandler, addRow, deleteRow, setRegistData] =
    useRegist(
      [
        {
          name: "",
          sex: "",
          phoneNumber: ["", "-", "", "-", ""]
          // idnumber: ["", "-", ""],
        },
      ],
      {
        name: "",
        sex: "",
        phoneNumber: ["", "-", "", "-", ""]
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
      let phoneNumber = participant.phoneNumber && participant.phoneNumber.split("-");
      
      return {
        participantId: participant.participantId,
        name: participant.name,
        sex: participant.gender, // 남성,여성인지 체크
        phoneNumber: participant.phoneNumber
          ? [
              phoneNumber[0],
              "-",
              phoneNumber[1],
              "-",
              phoneNumber[2],
            ]
          : [],
      };
    }

    if (mode === 2) {
      let phoneNumber = participant.phoneNumber.join("");
      if (phoneNumber === "--") phoneNumber = null;

      return {
        participantId: participant.participantId,
        name: participant.name,
        gender: participant.sex,
        eventId: EVENT_ID["자원봉사자"],
      };
    }
  };

  // 자원봉사자 페이지 들어오면 먼저 자원봉사자 저장된 데이터 있는지 체크
  const volunteerListHandler = useCallback(async () => {
    try {
      const responseData = await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/user/volunteer-list`,
        "GET",
        null,
        {
          Authorization: `Bearer ${auth.token}`,
        },
        "자원봉사자 정보 로드 실패"
      );

      // // TODO : change Dummy DATA
      // const responseData = {
      //   isSuccess: true,
      //   payload: {
      //     isParticipantExists: true,
      //     participants: [
      //       {
      //         participantId: 1,
      //         name: "조서영",
      //         gender: "여성",
      //         phoneNumber: "010-5137-8081",
      //         eventId: 11,
      //       },
      //     ],
      //   },
      // };
      // const responseData = {
      //   isSuccess: true,
      //   payload: { isParticipantExists: false },
      // };

      if (responseData.payload.isParticipantExists) {
        setIsRegistMode(false);
        setRegistData(
          responseData.payload.participants.map((participant) =>
            formatParticipant(participant, 1)
          )
        );
      } else {
        setIsRegistMode(true); //useRegist 초기값 정하기
        setIsFirst(true);
      }
      setApiFail(false);
    } catch (err) {
      setRegistData([]);
      setApiFail(true);
    }
  }, [auth.token, sendRequest, setRegistData]);

  const volunteerRegistHandler = async () => {
    try {
      // const responseData = await http.sendRequest(
      await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/user/volunteer-regist`,
        "POST",
        JSON.stringify({
          participants: registState.inputs.map((participant) =>
            formatParticipant(participant, 2)
          ),
        }),
        {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        },
        "자원봉사자 등록 실패"
      );
    } catch (err) {
      throw err;
    }
  };

  const switchModeHandler = (event) => {
    event.preventDefault();

    if (isRegistMode) {
      // submit 전에 참가자 데이터 유효성 검증
      let isValidity = true;
      let errMsg;
      const participantNumber = registState.inputs.length;
      for (let i = 0; i < participantNumber; i++) {
        const { result, message, focusCol } = checkValidityVolunteer(
          registState.inputs[i]
        );
        isValidity = isValidity & result;
        if (!isValidity) {
          errMsg = `${i + 1}번째 자원봉사자 : ` + message;
          // 포커스 틀린 컬럼으로
          document.getElementById(`row${i}${focusCol}`).focus();
          break;
        }
      }

      if (!participantNumber) {
        isValidity = false;
        errMsg = "자원봉사자를 한 명 이상 신청해주세요.";
      }

      if (!isValidity) {
        setError({ title: "입력정보 확인", detail: errMsg });
        return;
      }

      // register
      volunteerRegistHandler()
        .then(() => {
          // list get
          volunteerListHandler();
          setIsRegistMode(!isRegistMode);
        })
        .catch(() => {});
    } else {
      setIsRegistMode(!isRegistMode);
    }
  };

  // 컴포넌트 열자마자 리스트 불러오기
  useEffect(() => {
    volunteerListHandler();
  }, [volunteerListHandler]);

  return (
    <div className="regist-event">
      <h2 className="regist-event-title">
        {isRegistMode ? "자원봉사자 신청" : "자원봉사자 신청확인"}
      </h2>
      {isRegistMode ? (
        <form className="regist-form">
          <div className="regist-btn-add-row">
            <Button type="button" onClick={addRowHandler}>
              자원봉사자 추가
            </Button>
          </div>
          <RegistTable
            columns={TABLE_COLUMNS_REGIST_VOLUNTEER}
            data={registState.inputs}
            inputHandler={inputHandler}
            buttonHandler={deleteRowHandler}
            showNumber
          />
          <div className="regist-btn-submit">
            <Button type="button" onClick={switchModeHandler}>
              {isFirst ? "신청하기" : "수정완료"}
            </Button>
          </div>
        </form>
      ) : (
        <div className="regist-form">
          <RegistTable
            columns={TABLE_COLUMNS_CHECK_VOLUNTEER}
            data={registState.inputs}
            showNumber
          />
          <div className="check-btn-submit">
            <Button onClick={switchModeHandler} disabled={apiFail}>
              추가 및 수정하기
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};

export default RegistVolunteer;
