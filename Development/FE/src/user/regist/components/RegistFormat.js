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

  const [saveParticipant, setSaveParticipant] = useState([]);
  const [envPeriod, setEnvPeriod] = useState("none");
  const [isEditable, setIsEditable] = useState(false);
  const [enableDropdownRow, setEnableDropdownRow] = useState(null);

  const [registState, inputHandler, addRow, deleteRow, setRegistData] =
    useRegist([], props.newPersonFormat);

  const addRowHandler = (event) => {
    event.preventDefault();
    addRow();
  };

  const { formatParticipant, englishTitle, errMsgPersonName } = props;

  const periodGetHandler = useCallback(async () => {
    try {
      const responseData = await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/env/period`,
        "GET",
        null,
        {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        },
        `기간 호출 실패`
      );

      // // TODO: Remove Dummy data
      // const responseData = {
      //   isSuccess: true,
      //   payload: { period: "first" },
      // };

      if (!responseData.isSuccess) {
        setError({
          title: `기간 호출 실패`,
          detail: responseData.message,
        });
      } else {
        setEnvPeriod(responseData.payload.period);
      }
    } catch (err) {
      throw err;
    }
  }, [setError]);

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
          participantApplicationId:
            props.englishTitle === "individual"
              ? Object.values(saveParticipant[rowNum].eventInfo)
              : Object.values(saveParticipant[rowNum].eventInfo)[0],
        }),
        {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        },
        `${props.errMsgPersonName} 삭제 실패`
      );
      // const responseData = {
      //   isSuccess: true,
      // };

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
        `${process.env.REACT_APP_BACKEND_URL}/api/user/${englishTitle}?userId=${auth.userId}`,
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
      //     isEditable: true,
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
      //         eventInfo: { 1: 5, 2: 10 },
      //       },
      //       {
      //         participantId: 2,
      //         //weightClassId: ,
      //         name: "조땡땡",
      //         identityNumber: "961201-0000001",
      //         gender: "남성",
      //         isForeigner: true,
      //         nationality: "영국",
      //         eventInfo: { 4: 8 },
      //       },
      //     ],
      //   },
      // };
      // const responseData = {
      //   isSuccess: true,
      //   payload: { isParticipantExists: false },
      // };

      if (responseData.payload.isParticipantExists) {
        setIsEditable(responseData.payload.isEditable);
        setIsRegistMode(false);
        setRegistData(
          responseData.payload.participants.map((participant) =>
            formatParticipant(participant, 1)
          )
        );
        setSaveParticipant(responseData.payload.participants);
      } else if (responseData.payload.isEditable) {
        setIsRegistMode(true); //useRegist 초기값 정하기
        addRow();
      }
      setApiFail(false);
    } catch (err) {
      setRegistData([]);
      setApiFail(true);
    }
  }, [
    auth.token,
    auth.userId,
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

      const formatData = formatParticipant(
        participantData,
        3,
        saveParticipant[rowNum]
      );

      if (formatData) {
        const responseData = await sendRequest(
          `${process.env.REACT_APP_BACKEND_URL}/api/user/${props.englishTitle}`,
          "PUT",
          JSON.stringify({
            // userId: auth.userId,
            ...formatData,
          }),
          {
            Authorization: `Bearer ${auth.token}`,
            "Content-Type": "application/json",
          },

          `${props.errMsgPersonName} 수정 실패`
        );

        // DUMMY DATA
        // const responseData = {
        //   isSuccess: true,
        //   message: "check please",
        //   payload: {
        //     participantId: 2,
        //     // weightClassId: ,
        //     name: "조땡땡",
        //     identityNumber: "000000-0000001",
        //     gender: "남성",
        //     isForeigner: true,
        //     nationality: "영국",
        //     eventInfo: { 4: 8 },
        //   },
        // };

        if (responseData.isSuccess) {
          let participantsData = [...registState.inputs];
          participantsData[rowNum] = formatParticipant(responseData.payload, 1);
          if (props.englishTitle !== "individual") {
            participantsData[rowNum].eventInfo =
              registState.inputs[rowNum].eventInfo;
          }
          setRegistData(participantsData);

          let saveParticipantData = saveParticipant;
          saveParticipantData[rowNum] = responseData.payload;
          setSaveParticipant(saveParticipantData);
        } else {
          setError({
            title: `${props.errMsgPersonName} 수정 실패`,
            detail: responseData.message,
          });
        }
      } else {
        // let participantsData = registState.inputs;
        // participantsData[rowNum].editable = false;
        // setRegistData(participantsData);
        let participantsData = [...registState.inputs];

        participantsData[rowNum] = {
          ...participantsData[rowNum],
          editable: false,
        };
        setRegistData(participantsData);
        return;
      }
    } catch (err) {
      // throw err;
    }
  };

  const switchRowHandler = (event) => {
    event.preventDefault();
    const rowNum = Number(event.target.id.split("-")[0].replace("row", ""));

    let participantsData = [...registState.inputs];

    participantsData[rowNum] = {
      ...participantsData[rowNum],
      editable: true,
    };
    // participantsData[rowNum].editable = true;
    setRegistData(participantsData);

    //개인전 2차등록일 때 종목 2개이면 종목은 active 처리
    if (
      props.englishTitle === "individual" &&
      envPeriod === "second" &&
      registState.inputs[rowNum].event.length > 1
    ) {
      setEnableDropdownRow(rowNum); // Set the row number to enable the dropdown
    }
  };

  useEffect(() => {
    if (enableDropdownRow !== null) {
      const elementName = `row${enableDropdownRow}-col5-event`;
      document.getElementsByName(elementName).forEach((ele) => {
        ele.disabled = false;
      });
      setEnableDropdownRow(null); // Reset the state after enabling the dropdown
    }
  }, [enableDropdownRow]);

  const switchModeHandler = (event) => {
    event.preventDefault();

    if (isRegistMode) {
      // submit 전에 참가자 데이터 유효성 검증
      let isValidity = true;
      let errMsg;
      const participantNumber = registState.inputs.length;
      let isNewPeople = false;
      for (let i = 0; i < participantNumber; i++) {
        if (registState.inputs[i].isNew) {
          isNewPeople = true;
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
      }

      if (!isNewPeople) {
        isValidity = false;
        errMsg = `${props.personName} 한 명 이상 신청해주세요.`;
      }

      if (
        props.englishTitle === "second" &&
        participantNumber > process.env.REACT_APP_SECOND_NUM_LIMIT
      ) {
        isValidity = false;
        errMsg = `${props.personName}은 ${process.env.REACT_APP_SECOND_NUM_LIMIT}명 이하로 신청해주세요.`;
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
        setIsRegistMode(true);
        addRow();
      }
    }
  };

  // 컴포넌트 열자마자 리스트 불러오기
  useEffect(() => {
    periodGetHandler()
      .then(() => {
        // list get
        if (["first", "second"].includes(envPeriod)) {
          listHandler();
        }
      })
      .catch(() => {});
  }, [periodGetHandler, listHandler, envPeriod]);

  return (
    <div className="regist-event" id={`${props.englishTitle}-regist-event`}>
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
            isEditable={isEditable}
            checkColumns={props.checkTableColumn}
            data={registState.inputs}
            inputHandler={inputHandler}
            deleteHandler={deleteRowHandler}
            showNumber
          />
          <div className="regist-btn-submit">
            <Button type="button" onClick={switchModeHandler}>
              신청하기
            </Button>
          </div>
        </form>
      ) : (
        <div className="regist-form">
          <RegistTable
            version="check"
            columns={props.checkTableColumn}
            isEditable={isEditable}
            modifyColumns={
              envPeriod === "first"
                ? props.registTableColumn
                : props.registTableColumnSecondPeriod
            }
            data={registState.inputs}
            inputHandler={inputHandler}
            switchRowHandler={switchRowHandler}
            modifyHandler={modifyRowHandler}
            deleteHandler={deleteDataHandler}
            showNumber
          />
          {envPeriod === "first" && isEditable && (
            <div className="check-btn-submit">
              <Button onClick={switchModeHandler} disabled={apiFail}>
                추가하기
              </Button>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default RegistFormat;
