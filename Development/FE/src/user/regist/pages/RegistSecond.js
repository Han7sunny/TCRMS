import React, { useState, useContext, useEffect, useCallback } from "react";

import {
  TABLE_COLUMNS_REGIST_SECOND,
  TABLE_COLUMNS_CHECK_SECOND,
} from "../../../shared/util/regist-columns";
import { EVENT_ID } from "../../../shared/util/const-event";
import { checkValiditySecond } from "../../../shared/util/regist-validators";
import { useRegist } from "../../../shared/hooks/regist-hook";
import { HttpContext } from "../../../shared/context/http-context";
import { AuthContext } from "../../../shared/context/auth-context";

import RegistTable from "../components/RegistTable";
import Button from "../../../shared/components/TableInputElements/Button";

import "./RegistIndividual.css";

const RegistSecond = () => {
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
          foreigner: [],
          nationality: "",
          idnumber: ["", "-", ""],
        },
      ],
      {
        name: "",
        sex: "",
        foreigner: [],
        nationality: "",
        idnumber: ["", "-", ""],
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
      let idnumber = participant.identityNumber && participant.identityNumber.split("-");

      return {
        participantId: participant.participantId,
        name: participant.name,
        sex: participant.gender, // 남성,여성인지 체크
        foreigner: participant.isForeigner ? ["외국인"] : [],
        nationality: participant.nationality,
        idnumber: participant.identityNumber
          ? [
              idnumber[0],
              "-",
              idnumber[1],
            ]
          : [],
      };
    }

    if (mode === 2) {
      let identityNumber = participant.idnumber.join("");
      if (identityNumber === "-") identityNumber = null;

      return {
        participantId: participant.participantId,
        name: participant.name,
        gender: participant.sex,
        isForeigner: participant.foreigner.length > 0 ? true : false,
        nationality: participant.nationality,
        identityNumber: identityNumber,
        eventId: EVENT_ID["세컨"],
      };
    }
  };

  // 세컨 페이지 들어오면 먼저 세컨 저장된 데이터 있는지 체크
  const secondListHandler = useCallback(async () => {
    try {
      // const responseData = await sendRequest(
      //   `${process.env.REACT_APP_BACKEND_URL}/api/user/second-list`,
      //   "GET",
      //   null,
      //   {
      //     Authorization: `Bearer ${auth.token}`,
      //   },
      //   "세컨 정보 로드 실패"
      // );

      // TODO : change Dummy DATA
      const responseData = {
        isSuccess: true,
        payload: {
          isParticipantExists: true,
          participants: [
            {
              participantId: 1,
              name: "조서영",
              identityNumber: "961201-0000000",
              gender: "여성",
              isForeigner: false,
              nationality: "",
            },
            {
              participantId: 2,
              name: "조땡땡",
              identityNumber: "961201-0000001",
              gender: "남성",
              isForeigner: true,
              nationality: "영국",
            },
          ],
        },
      };
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

  const secondRegistHandler = async () => {
    try {
      await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/user/second-regist`,
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
        "세컨 등록 실패"
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
        const { result, message, focusCol } = checkValiditySecond(
          registState.inputs[i]
        );
        isValidity = isValidity & result;
        if (!isValidity) {
          errMsg = `${i + 1}번째 세컨 : ` + message;
          // 포커스 틀린 컬럼으로
          document.getElementById(`row${i}${focusCol}`).focus();
          break;
        }
      }

      if (!participantNumber) {
        isValidity = false;
        errMsg = "세컨을 한 명 이상 신청해주세요.";
      }

      if (!isValidity) {
        setError({ title: "입력정보 확인", detail: errMsg });
        return;
      }

      // register
      secondRegistHandler()
        .then(() => {
          // list get
          secondListHandler();
          setIsRegistMode(!isRegistMode);
        })
        .catch(() => {});
    } else {
      setIsRegistMode(!isRegistMode);
    }
  };

  // 컴포넌트 열자마자 리스트 불러오기
  useEffect(() => {
    secondListHandler();
  }, [secondListHandler]);

  return (
    <div className="regist-event">
      <h2 className="regist-event-title">
        {isRegistMode ? "세컨 신청" : "세컨 신청확인"}
      </h2>
      {isRegistMode ? (
        <form className="regist-form">
          <div className="regist-btn-add-row">
            <Button type="button" onClick={addRowHandler}>
              세컨 추가
            </Button>
          </div>
          <RegistTable
            columns={TABLE_COLUMNS_REGIST_SECOND}
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
            columns={TABLE_COLUMNS_CHECK_SECOND}
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

export default RegistSecond;