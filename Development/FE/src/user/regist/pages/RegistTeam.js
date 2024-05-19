import React, { useState, useContext, useEffect, useCallback } from "react";

import {
  TABLE_COLUMNS_REGIST_TEAM,
  TABLE_COLUMNS_CHECK_TEAM,
} from "../../../shared/util/regist-columns";
import { EVENT_ID, WEIGHT_ID } from "../../../shared/util/const-event";
import { checkValidityTeam } from "../../../shared/util/regist-validators";
import { useRegist } from "../../../shared/hooks/regist-hook";
import { HttpContext } from "../../../shared/context/http-context";
import { AuthContext } from "../../../shared/context/auth-context";

import RegistTeamTable from "../components/RegistTeamTable";
import Button from "../../../shared/components/TableInputElements/Button";

import "./RegistTeam.css";

// popup 에서 누르면 addRow하고 member 숫자만큼 데이터 추가하기
// 단체전 별 member 숫자는 util에서 정해주는 걸루

const RegistTeam = () => {
  const auth = useContext(AuthContext);
  const { sendRequest, setError } = useContext(HttpContext);

  const [isFirst, setIsFirst] = useState(true);
  const [isRegistMode, setIsRegistMode] = useState(false);
  const [apiFail, setApiFail] = useState(false);

  const errMsgPersonName = "팀";
  const englishTitle = "team";
  const checkValidity = checkValidityTeam;

  const [registState, inputHandler, addRow, deleteRow, setRegistData] =
    useRegist(
      [
        {
          eventTeamNumber: 2,
          event: 5,

          teamMembers: [
            {
              index: "1번 선수",
              name: "",
              // sex: "", 혼성도 적지 말아? 아니면 자동체크
              foreigner: [],
              nationality: "",
              idnumber: ["", "-", ""],
              weight: "",
            },
            {
              index: "2번 선수",
              name: "",
              // sex: "", 혼성도 적지 말아? 아니면 자동체크
              foreigner: [],
              nationality: "",
              idnumber: ["", "-", ""],
              weight: "",
            },
            {
              index: "3번 선수",
              name: "",
              // sex: "", 혼성도 적지 말아? 아니면 자동체크
              foreigner: [],
              nationality: "",
              idnumber: ["", "-", ""],
              weight: "",
            },
            {
              index: "후보 선수",
              name: "",
              // sex: "", 혼성도 적지 말아? 아니면 자동체크
              foreigner: [],
              nationality: "",
              idnumber: ["", "-", ""],
              weight: "",
            },
          ],
        },
      ],
      {
        eventTeamNumber: null,
        event: null,

        teamMembers: [],
      }
    );

  const addTeamHandler = (event) => {
    event.preventDefault();
    // modal 띄우기
    // addRow();
  };

  const deleteTeamHandler = (event) => {
    event.preventDefault();
    const teamNum = Number(event.target.id.split("-")[1].replace("team", ""));
    deleteRow(teamNum);
  };

  const deleteDataHandler = async (event) => {
    event.preventDefault();
    const teamNum = Number(event.target.id.split("-")[1].replace("team", ""));

    try {
      const responseData = await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/user/${englishTitle}`,
        "DELETE",
        JSON.stringify({
          userId: auth.userId,
          eventTeamNumber: registState.inputs[teamNum].eventTeamNumber,
        }),
        {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        },
        `${errMsgPersonName} 삭제 실패`
      );

      if (responseData.isSuccess) {
        deleteRow(teamNum);
      } else {
        setError({
          title: `${errMsgPersonName} 삭제 실패`,
          detail: responseData.message,
        });
      }
    } catch (error) {}
  };

  const formatTeam = (team, mode) => {
    if (mode === 1) {
      let eventName;
      if (team.eventId) {
        eventName = Object.keys(EVENT_ID).find(
          (key) => EVENT_ID[key].id === team.eventId
        );
      }

      const getWeight = (weightId, sex) => {
        let weight = "";
        if (weightId) {
          weight = Object.keys(WEIGHT_ID[sex]).find(
            (key) => WEIGHT_ID[sex][key] === weightId
          );
        }
        return weight;
      };

      const getIdNumber = (identityNumber) => {
        if (!identityNumber) return "";

        let idnumber = identityNumber.split("-");
        return [idnumber[0], "-", idnumber[1]];
      };

      return {
        eventTeamNumber: team.eventTeamNumber,
        event: EVENT_ID[eventName].name,
        editable: false,
        teamMembers: team.teamMembers.map((member) => {
          return {
            index: member.index,
            name: member.name,
            sex: member.gender,
            foreigner: member.isForeigner ? ["외국인"] : [],
            nationality: member.nationality,
            idnumber: getIdNumber(member.identityNumber),
            weight: getWeight(member.weightClassId, member.gender),
          };
        }),
      };
    }

    if (mode === 2) {
      let eventName;
      if (team.eventId) {
        eventName = Object.keys(EVENT_ID).find(
          (key) => EVENT_ID[key].name === team.event
        );
      }

      const getIdNumber = (idnumber) => {
        let identityNumber = idnumber.join("");
        if (identityNumber === "-") identityNumber = null;
        return identityNumber;
      };

      console.log(team);

      return {
        eventTeamNumber: team.eventTeamNumber,
        eventId: EVENT_ID[eventName].id,

        teamMembers: team.teamMembers.map((member) => {
          return {
            index: member.index,
            name: member.name,
            gender: member.sex,
            isForeigner: member.foreigner.length > 0 ? true : false,
            nationality: member.nationality,
            identityNumber: getIdNumber(member.idnumber),
            weightClassId: WEIGHT_ID[member.sex][member.weight],
          };
        }),
      };
    }
  };

  // 단체전 페이지 들어오면 먼저 단체전 저장된 데이터 있는지 체크
  const teamListHandler = useCallback(async () => {
    try {
      // const responseData = await sendRequest(
      //   `${process.env.REACT_APP_BACKEND_URL}/api/user/team-list`,
      //   "GET",
      //   null,
      //   {
      //     Authorization: `Bearer ${auth.token}`,
      //   },
      //   "단체전 선수 로드 실패"
      // );

      // // TODO : change Dummy DATA
      const responseData = {
        isSuccess: true,
        payload: {
          isTeamExists: true,
          teams: [
            {
              eventTeamNumber: 2,
              eventId: 5,

              teamMembers: [
                {
                  participantId: 1,
                  index: "1번 선수",
                  name: "조서영",
                  gender: "여성",
                  isForeigner: false,
                  nationality: "",
                  identityNumber: "961201-0000000",
                  weightClassId: 1,
                },
                {
                  participantId: 2,
                  index: "2번 선수",
                  name: "조투투",
                  gender: "여성",
                  isForeigner: false,
                  nationality: "",
                  identityNumber: "961202-0000000",
                  weightClassId: 5,
                },
                {
                  participantId: 3,
                  index: "3번 선수",
                  name: "조삼삼",
                  gender: "여성",
                  isForeigner: false,
                  nationality: "",
                  identityNumber: "961203-0000000",
                  weightClassId: 6,
                },
                {
                  participantId: 4,
                  index: "후보 선수",
                  name: "조후보",
                  gender: "여성",
                  isForeigner: false,
                  nationality: "",
                  identityNumber: "961204-0000000",
                  weightClassId: 9,
                },
              ],
            },
            {
              eventTeamNumber: 3,
              eventId: 8,

              teamMembers: [
                {
                  participantId: 1,
                  index: "1번 선수",
                  name: "조서영",
                  gender: "남성",
                  isForeigner: false,
                  nationality: "",
                  identityNumber: "961201-0000000",
                  weightClassId: 7,
                },
                {
                  participantId: 2,
                  index: "2번 선수",
                  name: "조투투",
                  gender: "남성",
                  isForeigner: false,
                  nationality: "",
                  identityNumber: "961202-0000000",
                  weightClassId: 8,
                },
                {
                  participantId: 3,
                  index: "3번 선수",
                  name: "조삼삼",
                  gender: "남성",
                  isForeigner: false,
                  nationality: "",
                  identityNumber: "961203-0000000",
                  weightClassId: 6,
                },
              ],
            },
          ],
        },
      };
      // const responseData = {
      //   isSuccess: true,
      //   payload: { isTeamExists: false },
      // };

      if (responseData.payload.isTeamExists) {
        setIsRegistMode(false);
        setRegistData(
          responseData.payload.teams.map((team) => formatTeam(team, 1))
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

  const teamRegistHandler = async () => {
    try {
      // const responseData = await http.sendRequest(
      await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/user/team-regist`,
        "POST",
        JSON.stringify({
          userId: auth.userId,
          teams: registState.inputs.map((team) => formatTeam(team, 2)),
        }),
        {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        },
        "단체전 선수 등록 실패"
      );
    } catch (err) {
      throw err;
    }
  };

  const modifyModeHandler = (event) => {
    event.preventDefault();
    const teamNum = Number(event.target.id.split("-")[1].replace("team", ""));
    let teamsData = registState.inputs;
    teamsData[teamNum].editable = true;
    setRegistData(teamsData);
  };

  const modifyTeamHandler = async (event) => {
    try {
      const teamNum = Number(event.target.id.split("-")[1].replace("team", ""));
      const teamData = registState.inputs[teamNum];

      // validity check
      const teamMemberNumber = teamData.teamMembers.length;

      for (let i = 0; i < teamMemberNumber; i++) {
        const { result, message, focusCol } = checkValidity(
          teamData.teamMembers[i]
        );
        if (!result) {
          // 포커스 틀린 컬럼으로
          document.getElementById(`team${teamNum}-row${i}${focusCol}`).focus();
          setError({ title: "입력정보 확인", detail: message });
          return;
        }
      }

      const responseData = await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/user/${englishTitle}`,
        "PUT",
        JSON.stringify({
          userId: auth.userId,
          teams: [formatTeam(teamData, 2)],
        }),
        {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        },

        `${errMsgPersonName} 수정 실패`
      );
      // const responseData = {
      //   isSuccess: true,
      //   message: "check please",
      // };

      if (responseData.isSuccess) {
        let teamsData = registState.inputs;
        teamsData[teamNum].editable = false;
        setRegistData(teamsData);
      } else {
        setError({
          title: `${errMsgPersonName} 수정 실패`,
          detail: responseData.message,
        });
      }
    } catch (err) {
      // throw err;
    }
  };

  const switchModeHandler = (event) => {
    event.preventDefault();

    if (isRegistMode) {
      // submit 전에 참가자 데이터 유효성 검증
      let isValidity = true;
      let errMsg;
      const teamNumber = registState.inputs.length;
      for (let i = 0; i < teamNumber; i++) {
        const { result, message, focusCol } = checkValidityTeam(
          registState.inputs[i]
        );
        isValidity = isValidity & result;
        if (!isValidity) {
          errMsg = `${i + 1}번째 팀 : ` + message;
          // 포커스 틀린 컬럼으로
          document.getElementById(`row${i}${focusCol}`).focus();
          break;
        }
      }

      if (!teamNumber) {
        isValidity = false;
        errMsg = "단체전 한 팀 이상 신청해주세요.";
      }

      if (!isValidity) {
        setError({ title: "입력정보 확인", detail: errMsg });
        return;
      }

      // register
      teamRegistHandler()
        .then(() => {
          // list get
          teamListHandler();
          // setIsRegistMode(!isRegistMode);
        })
        .catch(() => {});
    } else {
      setIsRegistMode(!isRegistMode);
    }
  };

  // 컴포넌트 열자마자 리스트 불러오기
  useEffect(() => {
    teamListHandler();
  }, [teamListHandler]);

  return (
    <div className="regist-event">
      <h2 className="regist-event-title">
        {isRegistMode ? "단체전 신청" : "단체전 신청확인"}
      </h2>
      {isRegistMode ? (
        <form className="regist-form">
          <div className="regist-btn-add-row">
            <Button type="button" onClick={addTeamHandler}>
              팀 추가
            </Button>
          </div>
          {registState.inputs.map((team, i) => (
            <div className="regist-team" key={team.eventTeamNumber}>
              <div className="regist-team-subtitle">
                <div>{team.event}</div>
                <Button
                  id={`btn-team${i}-delete`}
                  onClick={deleteTeamHandler}
                  type="button"
                >
                  팀 삭제
                </Button>
              </div>
              <RegistTeamTable
                columns={TABLE_COLUMNS_REGIST_TEAM}
                data={team.teamMembers}
                inputHandler={inputHandler}
                teamId={`team${i}-`}
              />
            </div>
          ))}

          <div className="regist-btn-submit">
            <Button type="button" onClick={switchModeHandler}>
              {isFirst ? "신청하기" : "수정완료"}
            </Button>
          </div>
        </form>
      ) : (
        <div className="regist-form">
          {registState.inputs.map((team, i) => (
            <div className="regist-team" key={team.eventTeamNumber}>
              <div className="regist-team-subtitle">
                <div>{team.event}</div>
                {team.editable ? (
                  <React.Fragment>
                    <Button
                      id={`btn-team${i}-modify`}
                      className="btn-team-modify"
                      onClick={modifyTeamHandler}
                      type="button"
                    >
                      수정완료
                    </Button>
                    <Button
                      id={`btn-team${i}-delete`}
                      className="btn-team-delete"
                      onClick={deleteDataHandler}
                      type="button"
                    >
                      삭제하기
                    </Button>
                  </React.Fragment>
                ) : (
                  <Button
                    id={`btn-team${i}-modechange`}
                    className="btn-team-modechange"
                    onClick={modifyModeHandler}
                    type="button"
                  >
                    수정하기
                  </Button>
                )}
              </div>
              <RegistTeamTable
                version="check"
                columns={TABLE_COLUMNS_CHECK_TEAM}
                modifyColumns={TABLE_COLUMNS_REGIST_TEAM}
                data={team.teamMembers}
                inputHandler={inputHandler}
                editMode={team.editable}
                teamId={`team${i}-`}
              />
            </div>
          ))}
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

export default RegistTeam;
