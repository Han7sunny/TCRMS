import React, { useState, useContext, useEffect, useCallback } from "react";

import { useRegist } from "../../../shared/hooks/regist-hook";
import { HttpContext } from "../../../shared/context/http-context";
import { AuthContext } from "../../../shared/context/auth-context";

import RegistTable from "../components/RegistTable";
import Button from "../../../shared/components/TableInputElements/Button";

import "./RegistFormat.css";

const RegistFormat = (props) => {
  const auth = useContext(AuthContext);
  const { sendRequest, setError } = useContext(HttpContext);

  const [isRegistMode, setIsRegistMode] = useState(false);
  const [apiFail, setApiFail] = useState(false);

  const [registState, inputHandler, addRow, deleteRow, setRegistData] =
    useRegist([], props.newPersonFormat);

  const addRowHandler = (event) => {
    event.preventDefault();
    addRow();
  };

  const { formatParticipant, englishTitle, errMsgPersonName } = props;

  const deleteDataHandler = async (event) => {
    event.preventDefault();
    const rowNum = Number(event.target.id.split("-")[0].replace("row", ""));

    try {
      const responseData = await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/user/${props.englishTitle}`,
        "DELETE",
        JSON.stringify({
          userId: auth.userId,
          participantId: registState.inputs[rowNum].participantId,
        }),
        {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        },
        `${props.errMsgPersonName} 삭제 실패`
      );

      if (responseData.isSuccess) {
        deleteRow(rowNum);
      } else {
        setError({
          title: `${props.errMsgPersonName} 삭제 실패`,
          detail: responseData.message,
        });
      }
    } catch (error) {}
  };

  const deleteRowHandler = async (event) => {
    event.preventDefault();
    const rowNum = Number(event.target.id.split("-")[0].replace("row", ""));
    deleteRow(rowNum);
  };

  // 개인전 페이지 들어오면 먼저 개인전 저장된 데이터 있는지 체크
  const listHandler = useCallback(async () => {
    try {
      const responseData = await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/user/${englishTitle}`,
        "GET",
        null,
        {
          Authorization: `Bearer ${auth.token}`,
        },
        `${errMsgPersonName} 로드 실패`
      );

      // // TODO : change Dummy DATA
      // const responseData = {
      //   isSuccess: true,
      //   payload: {
      //     isParticipantExists: true,
      //     participants: [
      //       {
      //         participantId: 1,
      //         weightClassId: 1,
      //         name: "조서영",
      //         identityNumber: "961201-0000000",
      //         gender: "여성",
      //         isForeigner: false,
      //         phoneNumber: "010-5137-8081",
      //         nationality: "",
      //         eventId: [1, 2],
      //       },
      //       {
      //         participantId: 2,
      //         //weightClassId: ,
      //         name: "조땡땡",
      //         identityNumber: "961201-0000001",
      //         gender: "남성",
      //         isForeigner: true,
      //         nationality: "영국",
      //         eventId: [4],
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
        addRow();
        // setIsFirst(true);
      }
      setApiFail(false);
    } catch (err) {
      setRegistData([]);
      setApiFail(true);
    }
  }, [
    auth.token,
    sendRequest,
    setRegistData,
    addRow,
    formatParticipant,
    englishTitle,
    errMsgPersonName,
  ]);

  const registHandler = async () => {
    try {
      const responseData = await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/user/${props.englishTitle}`,
        "POST",
        JSON.stringify({
          userId: auth.userId,
          participants: registState.inputs
            .filter((participant) => participant.isNew)
            .map((participant) => formatParticipant(participant, 2)),
        }),
        {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        },
        `${props.errMsgPersonName} 등록 실패`
      );

      // // TODO: Remove Dummy data
      // const responseData = {
      //   isSuccess: true,
      // };
      if (!responseData.isSuccess) {
        setError({
          title: `${props.errMsgPersonName} 등록 실패`,
          detail: responseData.message,
        });
      }
    } catch (err) {
      throw err;
    }
  };

  const modifyRowHandler = async (event) => {
    try {
      const rowNum = Number(event.target.id.split("-")[0].replace("row", ""));
      const participantData = registState.inputs[rowNum];

      // validity check
      const { result, message, focusCol } =
        props.checkValidity(participantData);
      if (!result) {
        // 포커스 틀린 컬럼으로
        document.getElementById(`row${rowNum}${focusCol}`).focus();
        setError({ title: "입력정보 확인", detail: message });
        return;
      }

      const responseData = await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/user/${props.englishTitle}`,
        "PUT",
        JSON.stringify({
          userId: auth.userId,
          participants: [formatParticipant(participantData, 2)],
        }),
        {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        },

        `${props.errMsgPersonName} 수정 실패`
      );
      // const responseData = {
      //   isSuccess: true,
      //   message: "check please",
      // };

      if (responseData.isSuccess) {
        let participantsData = registState.inputs;
        participantsData[rowNum].editable = false;
        setRegistData(participantsData);
      } else {
        setError({
          title: `${props.errMsgPersonName} 수정 실패`,
          detail: responseData.message,
        });
      }
    } catch (err) {
      // throw err;
    }
  };

  const switchRowHanlder = (event) => {
    event.preventDefault();
    const rowNum = Number(event.target.id.split("-")[0].replace("row", ""));
    let participantsData = registState.inputs;
    participantsData[rowNum].editable = true;
    setRegistData(participantsData);
  };

  const switchModeHandler = (event) => {
    event.preventDefault();

    if (isRegistMode) {
      // submit 전에 참가자 데이터 유효성 검증
      let isValidity = true;
      let errMsg;
      const participantNumber = registState.inputs.length;
      for (let i = 0; i < participantNumber; i++) {
        const { result, message, focusCol } = props.checkValidity(
          registState.inputs[i]
        );
        isValidity = isValidity & result;
        if (!isValidity) {
          errMsg = `${i + 1}번째 ${props.personName} : ` + message;
          // 포커스 틀린 컬럼으로
          document.getElementById(`row${i}${focusCol}`).focus();
          break;
        }
      }

      if (!participantNumber) {
        isValidity = false;
        errMsg = `${props.personName} 한 명 이상 신청해주세요.`;
      }

      if (!isValidity) {
        setError({ title: "입력정보 확인", detail: errMsg });
        return;
      }

      // register
      registHandler()
        .then(() => {
          // list get
          listHandler();
        })
        .catch(() => {});
    } else {
      let isEditting = false;

      registState.inputs.forEach((participant) => {
        isEditting = isEditting || participant.editable;
      });
      if (isEditting) {
        setError({
          title: "",
          detail: "수정 완료 후 추가하기 버튼을 눌러주세요.",
        });
      } else {
        setIsRegistMode(!isRegistMode);
        addRow();
      }
    }
  };

  // 컴포넌트 열자마자 리스트 불러오기
  useEffect(() => {
    listHandler();
  }, [listHandler]);

  return (
    <div className="regist-event">
      <h2 className="regist-event-title">
        {isRegistMode
          ? `${props.koreanTitle} 신청`
          : `${props.koreanTitle} 신청확인`}
      </h2>
      {isRegistMode ? (
        <form className="regist-form">
          <div className="regist-btn-add-row">
            <Button type="button" onClick={addRowHandler}>
              {props.personName} 추가
            </Button>
          </div>
          <RegistTable
            version="regist"
            columns={props.registTableColumn}
            checkColumns={props.checkTableColumn}
            data={registState.inputs}
            inputHandler={inputHandler}
            deleteHandler={deleteRowHandler}
            showNumber
          />
          <div className="regist-btn-submit">
            <Button type="button" onClick={switchModeHandler}>
              {/* {isFirst ? "신청하기" : "수정완료"} */}
              신청하기
            </Button>
          </div>
        </form>
      ) : (
        <div className="regist-form">
          <RegistTable
            version="check"
            columns={props.checkTableColumn}
            modifyColumns={props.registTableColumn}
            data={registState.inputs}
            inputHandler={inputHandler}
            switchRowHanlder={switchRowHanlder}
            modifyHandler={modifyRowHandler}
            deleteHandler={deleteDataHandler}
            showNumber
          />
          <div className="check-btn-submit">
            <Button onClick={switchModeHandler} disabled={apiFail}>
              추가하기
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};

export default RegistFormat;
