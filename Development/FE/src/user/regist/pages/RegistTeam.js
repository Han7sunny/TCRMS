import React, { useState, useContext, useEffect, useCallback } from "react";

import {
  TABLE_COLUMNS_CHECK_TEAM,
  TABLE_COLUMNS_REGIST_TEAM_SPARRING,
  TABLE_COLUMNS_REGIST_TEAM_FORM,
  TABLE_COLUMNS_REGIST_PERIOD2_TEAM_FORM,
  TABLE_COLUMNS_REGIST_PERIOD2_TEAM_SPARRING,
} from "../../../shared/util/regist/regist-columns";
import { EVENT_ID, WEIGHT_ID } from "../../../shared/util/const-event";
import { checkValidityTeam } from "../../../shared/util/regist/regist-validators";
import { useRegist } from "../../../shared/hooks/regist-hook";
import { HttpContext } from "../../../shared/context/http-context";
import { AuthContext } from "../../../shared/context/auth-context";

import RegistTeamTable from "../components/RegistTeamTable";
import Button from "../../../shared/components/TableInputElements/Button";

import "./RegistTeam.css";
import AddTeamModal from "../components/AddTeamModal";

// popup 에서 누르면 addRow하고 member 숫자만큼 데이터 추가하기
// 단체전 별 member 숫자는 util에서 정해주는 걸루

const RegistTeam = () => {
  const auth = useContext(AuthContext);
  const { sendRequest, setError } = useContext(HttpContext);

  const [isRegistMode, setIsRegistMode] = useState(false);
  const [apiFail, setApiFail] = useState(false);
  const [teamSelectModalShow, setTeamSelectModalShow] = useState(false);

  const [saveTeam, setSaveTeam] = useState([]);
  const [envPeriod, setEnvPeriod] = useState("NONE");
  const [isEditable, setIsEditable] = useState(false);

  const errMsgPersonName = "팀";
  const englishTitle = "team";
  const checkValidity = (teamNum) => {
    const teamData = registState.inputs[teamNum];

    // each teamMember validity check
    const teamMemberNumber = teamData.teamMembers.length;
    for (let i = 0; i < teamMemberNumber; i++) {
      if (teamData.teamMembers[i].editable) {
        const { result, message, focusCol } = checkValidityTeam(
          teamData.teamMembers[i],
          teamData.event
        );
        if (!result) {
          // 포커스 틀린 컬럼으로
          document.getElementById(`team${teamNum}-row${i}${focusCol}`).focus();
          return {
            isValidity: false,
            message: message,
            teamMemberIndex: teamData.teamMembers[i].indexInTeam,
          };
        }
      }
    }

    // 겨루기인 경우 단체전 체급 합산 체크
    if (teamData.event.includes("겨루기")) {
      const weights = teamData.teamMembers
        .filter((member) => member.editable)
        .map((member) => WEIGHT_ID[member.sex][member.weight].maxWeight);

      let sumWeightOK = true;
      if (weights.length > 3) {
        const sum = weights.reduce((a, b) => a + b, 0);
        const min = Math.min.apply(null, weights);
        if (teamData.event.includes("남성")) {
          if (
            sum > process.env.REACT_APP_TEAM_SPARRING_WEIGHT_MALE_MEM4_LIMIT ||
            sum - min >
              process.env.REACT_APP_TEAM_SPARRING_WEIGHT_MALE_MEM3_LIMIT
          ) {
            sumWeightOK = false;
          }
        } else {
          if (
            sum >
              process.env.REACT_APP_TEAM_SPARRING_WEIGHT_FEMALE_MEM4_LIMIT ||
            sum - min >
              process.env.REACT_APP_TEAM_SPARRING_WEIGHT_FEMALE_MEM3_LIMIT
          ) {
            sumWeightOK = false;
          }
        }
      } else {
        const sum = weights.reduce((a, b) => a + b, 0);
        if (teamData.event.includes("남성")) {
          if (
            sum > process.env.REACT_APP_TEAM_SPARRING_WEIGHT_MALE_MEM3_LIMIT
          ) {
            sumWeightOK = false;
          }
        } else {
          if (
            sum > process.env.REACT_APP_TEAM_SPARRING_WEIGHT_FEMALE_MEM3_LIMIT
          ) {
            sumWeightOK = false;
          }
        }
      }

      if (!sumWeightOK) {
        return {
          isValidity: false,
          message: "체중 조건을 맞춰주세요",
        };
      }
    }

    return { isValidity: true };
  };

  const formatTeam = (team, mode, saveTeam) => {
    if (mode === 1) {
      // list (GET)
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
            (key) => WEIGHT_ID[sex][key].id === weightId
          );
        }
        return weight;
      };

      const getIdNumber = (identityNumber) => {
        if (!identityNumber) return ["", "-", ""];

        let idnumber = identityNumber.split("-");
        return [idnumber[0], "-", idnumber[1]];
      };

      let teamMembers = team.teamMembers.map((member) => {
        return {
          participantId: member.participantId,
          participantApplicationId: member.participantApplicationId,
          indexInTeam: member.indexInTeam,
          name: member.name,
          sex: member.gender,
          foreigner: member.isForeigner ? ["외국인"] : [],
          nationality: member.nationality,
          idnumber: getIdNumber(member.identityNumber),
          weight: getWeight(member.weightClassId, member.gender),
          phoneNumber: member.phoneNumber,
          editable: member.editable === false ? false : true,
        };
      });

      return {
        eventTeamNumber: team.eventTeamNumber,
        event: EVENT_ID[eventName].name,
        eventId: team.eventId,
        editable: false,
        teamMembers: teamMembers,
      };
    }

    if (mode === 2 || mode === 3) {
      let eventName;
      if (team.event) {
        eventName = Object.keys(EVENT_ID).find(
          (key) => EVENT_ID[key].name === team.event
        );
      }

      const getIdNumber = (idnumber) => {
        let identityNumber = idnumber.join("");
        if (identityNumber === "-") identityNumber = null;
        return identityNumber;
      };

      let sendData = {
        // eventTeamNumber: team.eventTeamNumber,
        eventId: EVENT_ID[eventName].id,

        teamMembers: team.teamMembers
          .filter((member) => member.editable)
          .map((member) => ({
            indexInTeam: member.indexInTeam,
            name: member.name,
            gender: member.sex,
            isForeigner: member.foreigner.length > 0 ? true : false,
            nationality: member.nationality,
            identityNumber: getIdNumber(member.idnumber),
            phoneNumber: member.phoneNumber,
            weightClassId: WEIGHT_ID[member.sex][member.weight].id,
          })),
      };

      if (mode === 2) {
        return sendData;
      }

      if (mode === 3) {
        const isNullData = (data) => {
          if (data === null || data === "" || data === undefined) return true;
          else return data;
        };

        let isChange = false;

        sendData.teamMembers = sendData.teamMembers.map((member) => {
          // participantId, isParticipantChange, participantApplicationId, isWeightClassChange
          const savedMember = saveTeam.teamMembers.filter(
            (saveMember) => saveMember.indexInTeam === member.indexInTeam
          )[0];

          let isParticipantChange = false;
          let isWeightClassChange = false;

          if (
            member.name !== savedMember.name ||
            member.gender !== savedMember.gender ||
            member.isForeigner !== savedMember.isForeigner ||
            isNullData(member.nationality) !==
              isNullData(savedMember.nationality) ||
            isNullData(member.identityNumber) !==
              isNullData(savedMember.identityNumber) ||
            isNullData(member.phoneNumber) !==
              isNullData(savedMember.phoneNumber)
          ) {
            isParticipantChange = true;
            isChange = true;
          }

          if (
            isNullData(member.weightClassId) !==
            isNullData(savedMember.weightClassId)
          ) {
            isWeightClassChange = true;
            isChange = true;
          }

          return {
            participantId: savedMember.participantId,
            participantApplicationId: savedMember.participantApplicationId,
            ...member,
            isParticipantChange: isParticipantChange,
            isWeightClassChange: isWeightClassChange,
          };
        });

        if (!isChange) {
          return false;
        }

        return {
          eventTeamNumber: team.eventTeamNumber,
          ...sendData,
        };
      }
    }
  };

  const [registState, inputHandler, addRow, deleteRow, setRegistData] =
    useRegist([], {
      eventTeamNumber: null,
      event: null,

      teamMembers: [],
    });

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
  }, [setError, auth.token, sendRequest]);

  const addTeamHandler = (event) => {
    event.preventDefault();
    // modal 띄우기
    setTeamSelectModalShow(true);
    // popup 에서 누르면 addRow하고 member 숫자만큼 데이터 추가하기(util 파라미터 이용)
  };

  const closeSelectModal = (event) => {
    event.preventDefault();
    setTeamSelectModalShow(false);
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
          // eventTeamNumber: registState.inputs[teamNum].eventTeamNumber,
          participantIds: registState.inputs[teamNum].teamMembers.map(
            (member) => member.participantId
          ),
          participantApplicationIds: registState.inputs[
            teamNum
          ].teamMembers.map((member) => member.participantApplicationId),
        }),
        {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        },
        `${errMsgPersonName} 삭제 실패`
      );
      // DUMMY DATA
      // const responseData = {
      //   isSuccess: true,
      //   message: "NO"
      // }

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

  // 단체전 페이지 들어오면 먼저 단체전 저장된 데이터 있는지 체크
  const teamListHandler = useCallback(async () => {
    try {
      let responseData = await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/user/${englishTitle}?userId=${auth.userId}`,
        "GET",
        null,
        {
          Authorization: `Bearer ${auth.token}`,
        },
        "단체전 선수 로드 실패"
      );

      // // // TODO : change Dummy DATA
      // let responseData = {
      //   isSuccess: true,
      //   payload: {
      //     isTeamExists: true,
      //     isEditable: true,
      //     teams: [
      //       {
      //         eventTeamNumber: 2,
      //         eventId: 5,
      //         teamMembers: [
      //           {
      //             participantId: 1,
      //             participantApplicationId: 100,
      //             indexInTeam: "1번 선수",
      //             name: "조서영",
      //             gender: "여성",
      //             isForeigner: false,
      //             nationality: "",
      //             identityNumber: "961201-0000000",
      //             weightClassId: 2,
      //             phoneNumber: "010-0000-0000",
      //           },
      //           {
      //             participantId: 2,
      //             participantApplicationId: 101,
      //             indexInTeam: "2번 선수",
      //             name: "조투투",
      //             gender: "여성",
      //             isForeigner: false,
      //             nationality: "",
      //             identityNumber: "961202-0000000",
      //             weightClassId: 3,
      //             phoneNumber: "",
      //           },
      //           {
      //             participantId: 3,
      //             participantApplicationId: 103,
      //             indexInTeam: "3번 선수",
      //             name: "조삼삼",
      //             gender: "여성",
      //             isForeigner: false,
      //             nationality: "",
      //             identityNumber: "961203-0000000",
      //             weightClassId: 4,
      //             phoneNumber: "",
      //           },
      //           {
      //             participantId: 4,
      //             participantApplicationId: 104,
      //             indexInTeam: "후보 선수",
      //             name: "조후보",
      //             gender: "여성",
      //             isForeigner: false,
      //             nationality: "",
      //             identityNumber: "961204-0000000",
      //             weightClassId: 2,
      //             phoneNumber: "",
      //           },
      //         ],
      //       },
      //       {
      //         eventTeamNumber: 4,
      //         eventId: 7,
      //         teamMembers: [
      //           {
      //             participantId: 1,
      //             indexInTeam: "1번 선수",
      //             name: "조서영",
      //             gender: "남성",
      //             isForeigner: false,
      //             nationality: "",
      //             identityNumber: "961201-0000000",
      //             weightClassId: 2,
      //             phoneNumber: "010-0000-0000",
      //           },
      //           {
      //             participantId: 2,
      //             indexInTeam: "2번 선수",
      //             name: "조투투",
      //             gender: "남성",
      //             isForeigner: false,
      //             nationality: "",
      //             identityNumber: "961202-0000000",
      //             weightClassId: 3,
      //             phoneNumber: "",
      //           },
      //           {
      //             participantId: 3,
      //             indexInTeam: "3번 선수",
      //             name: "조삼삼",
      //             gender: "남성",
      //             isForeigner: false,
      //             nationality: "",
      //             identityNumber: "961203-0000000",
      //             weightClassId: 4,
      //             phoneNumber: "",
      //           },
      //         ],
      //       },
      // ],
      // },
      // };
      // const responseData = {
      //   isSuccess: true,
      //   payload: { isTeamExists: false },
      // };

      if (responseData.payload.isTeamExists) {
        // 겨루기 단체전이고 후보선수 없을 경우 후보선수 만들어줘야함
        responseData.payload.teams = responseData.payload.teams.map((team) => {
          const eventName = Object.values(EVENT_ID).find(
            (event) => event.id === team.eventId
          ).name;
          if (
            eventName.includes("겨루기") &&
            team.teamMembers.filter(
              (member) => member.indexInTeam === "후보 선수"
            ).length === 0
          ) {
            team.teamMembers.push({
              indexInTeam: "후보 선수",
              name: "",
              // gender: eventName.includes("남성") ? "남성" : "여성",
              gender: "",
              isForeigner: false,
              nationality: "",
              identityNumber: "",
              phoneNumber: "",
              //weight: "",
              editable: false,
            });
          }

          return team;
        });

        setIsEditable(responseData.payload.isEditable);
        setIsRegistMode(false);
        setRegistData(
          responseData.payload.teams.map((team) => formatTeam(team, 1))
        );
        setSaveTeam(responseData.payload.teams);
      } else if (responseData.payload.isEditable) {
        setIsRegistMode(true);
        setTeamSelectModalShow(true);
      }
      setApiFail(false);
    } catch (err) {
      setRegistData([]);
      setApiFail(true);
    }
  }, [auth.token, auth.userId, sendRequest, setRegistData]);

  const teamRegistHandler = async () => {
    try {
      const responseData = await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/user/${englishTitle}`,
        "POST",
        JSON.stringify({
          userId: auth.userId,
          teams: registState.inputs
            .filter((team) => team.editable)
            .map((team) => formatTeam(team, 2)),
        }),
        {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        },
        "단체전 선수 등록 실패"
      );
      if (!responseData.isSuccess) {
        setError({
          title: `${errMsgPersonName} 등록 실패`,
          detail: responseData.message,
        });
      }
    } catch (err) {
      throw err;
    }
  };

  const modifyModeHandler = (event) => {
    event.preventDefault();
    const teamNum = Number(event.target.id.split("-")[1].replace("team", ""));
    let teamsData = registState.inputs;
    let teamData = teamsData[teamNum];
    teamData.editable = true;
    teamData.teaMembers = teamData.teamMembers.map((member) => {
      if (member.indexInTeam === "후보 선수" && member.sex === "") {
        member.sex = teamData.event.includes("남성") ? "남성" : "여성";
      }
      return member;
    });

    setRegistData(teamsData);
  };

  const modifyTeamHandler = async (event) => {
    const teamNum = Number(event.target.id.split("-")[1].replace("team", ""));
    const teamData = registState.inputs[teamNum];

    const { isValidity, message, teamMemberIndex } = checkValidity(teamNum);

    if (!isValidity) {
      setError({
        title: "입력정보 확인",
        detail: teamMemberIndex
          ? `${teamMemberIndex} : ${message}`
          : `${message}`,
      });
      return;
    }

    const formatData = formatTeam(teamData, 3, saveTeam[teamNum]);

    if (formatData) {
      try {
        const responseData = await sendRequest(
          `${process.env.REACT_APP_BACKEND_URL}/api/user/${englishTitle}`,
          "PUT",
          JSON.stringify({
            // userId: auth.userId,
            ...formatData,
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
          teamsData[teamNum] = formatTeam(responseData.payload, 1);
          setRegistData(teamsData);

          let saveTeamData = saveTeam;
          saveTeamData[teamNum] = responseData.payload;
          setSaveTeam(saveTeamData);
        } else {
          setError({
            title: `${errMsgPersonName} 수정 실패`,
            detail: responseData.message,
          });
        }
      } catch (err) {
        // throw err;
      }
    } else {
      // let teamsData = registState.inputs;
      // teamsData[teamNum].editable = false;
      // setRegistData(teamsData);
      let teamsData = registState.inputs;
      teamsData[teamNum] = formatTeam(saveTeam[teamNum], 1);
      setRegistData(teamsData);
      // setRegistData(saveTeam.map((team) => formatTeam(team, 1)));
      return;
    }
  };

  const switchModeHandler = (event) => {
    event.preventDefault();

    if (isRegistMode) {
      // submit 전에 참가자 데이터 유효성 검증
      let isValidity = true;
      let errMsg;
      const teamNumber = registState.inputs.length;
      let isNewTeam = false;

      for (let i = 0; i < teamNumber; i++) {
        if (registState.inputs[i].editable) {
          isNewTeam = true;
          const {
            isValidity: isTeamValid,
            message,
            teamMemberIndex,
          } = checkValidity(i);
          isValidity = isValidity & isTeamValid;
          if (!isTeamValid) {
            errMsg = teamMemberIndex
              ? `${i + 1}번째 팀의 ${teamMemberIndex} : ` + message
              : `${i + 1}번째 팀 : ` + message;
            break;
          }
        }
      }

      if (!isNewTeam) {
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
        })
        .catch(() => {});
    } else {
      let isEditting = false;

      registState.inputs.forEach((team) => {
        isEditting = isEditting || team.editable;
      });
      if (isEditting) {
        setError({
          title: "",
          detail: "수정 완료 후 추가하기 버튼을 눌러주세요.",
        });
      } else {
        setIsRegistMode(true);
        setTeamSelectModalShow(true);
      }
    }
  };

  // 컴포넌트 열자마자 리스트 불러오기
  useEffect(() => {
    periodGetHandler()
      .then(() => {
        // list get
        if (["FIRST", "SECOND"].includes(envPeriod)) {
          teamListHandler();
        }
      })
      .catch(() => {});
  }, [teamListHandler, periodGetHandler, envPeriod]);

  const addTeamCloseModalHandler = (eventName) => {
    addRow(eventName);
    setTeamSelectModalShow(false);
  };

  return (
    <div className="regist-team-event regist-event" id="team-regist-event">
      <AddTeamModal
        show={teamSelectModalShow}
        onClear={closeSelectModal}
        onClick={addTeamCloseModalHandler}
      />
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
            <div className="regist-team" key={`team${i}`}>
              <div className="regist-team-subtitle">
                <div>
                  <span className="regist-team-teamNumber">{i + 1}팀</span>
                  {team.event}
                </div>
                {team.editable && (
                  <Button
                    id={`btn-team${i}-delete`}
                    onClick={deleteTeamHandler}
                    type="button"
                  >
                    팀 삭제
                  </Button>
                )}
              </div>
              <RegistTeamTable
                columns={TABLE_COLUMNS_CHECK_TEAM}
                modifyColumns={
                  typeof team.event === "string" &&
                  team.event.includes("겨루기")
                    ? TABLE_COLUMNS_REGIST_TEAM_SPARRING
                    : TABLE_COLUMNS_REGIST_TEAM_FORM
                }
                data={team.teamMembers}
                inputHandler={inputHandler}
                editMode={team.editable}
                teamId={`team${i}-`}
              />
            </div>
          ))}

          <div className="regist-btn-submit">
            <Button type="button" onClick={switchModeHandler}>
              신청하기
            </Button>
          </div>
        </form>
      ) : (
        <div className="regist-form">
          {registState.inputs.map((team, i) => {
            return (
              <div className="regist-team" key={`team${i}`}>
                <div className="regist-team-subtitle">
                  <div>
                    <span className="regist-team-teamNumber">{i + 1}팀</span>
                    {team.event}
                  </div>

                  {isEditable &&
                    (team.editable ? (
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
                    ))}
                </div>
                <RegistTeamTable
                  columns={TABLE_COLUMNS_CHECK_TEAM}
                  modifyColumns={
                    typeof team.event === "string" &&
                    team.event.includes("겨루기")
                      ? envPeriod === "FIRST"
                        ? TABLE_COLUMNS_REGIST_TEAM_SPARRING
                        : TABLE_COLUMNS_REGIST_PERIOD2_TEAM_SPARRING
                      : envPeriod === "FIRST"
                      ? TABLE_COLUMNS_REGIST_TEAM_FORM
                      : TABLE_COLUMNS_REGIST_PERIOD2_TEAM_FORM
                  }
                  data={team.teamMembers}
                  inputHandler={inputHandler}
                  editMode={team.editable}
                  teamId={`team${i}-`}
                />
              </div>
            );
          })}
          {envPeriod === "FIRST" && isEditable && (
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

export default RegistTeam;
