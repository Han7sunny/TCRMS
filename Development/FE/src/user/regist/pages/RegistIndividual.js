import React, { useCallback } from "react";

import {
  TABLE_COLUMNS_REGIST_INDIVIDUAL,
  TABLE_COLUMNS_REGIST_PERIOD2_INDIVIDUAL,
  TABLE_COLUMNS_CHECK_INDIVIDUAL,
} from "../../../shared/util/regist-columns";
import { EVENT_ID, WEIGHT_ID } from "../../../shared/util/const-event";
import { checkValidityIndividual } from "../../../shared/util/regist-validators";

import RegistFormat from "../components/RegistFormat";

const RegistIndividual = () => {
  const formatParticipant = useCallback(
    (participant, mode, saveParticipant) => {
      if (mode === 1) {
        // list (GET)
        let event = new Set();

        const eventIds = Object.keys(participant.eventInfo);
        if (eventIds.length) {
          eventIds.forEach((eventId) => {
            let eventName = Object.keys(EVENT_ID).find(
              (key) => EVENT_ID[key].id === Number(eventId)
            );
            event.add(eventName.split(" ")[2]);
          });
        }
        event = [...event];

        let weight = "";
        if (participant.weightClassId) {
          weight = Object.keys(WEIGHT_ID[participant.gender]).find(
            (key) =>
              WEIGHT_ID[participant.gender][key] === participant.weightClassId
          );
        }

        let idnumber =
          participant.identityNumber && participant.identityNumber.split("-");

        return {
          participantId: participant.participantId,
          eventInfo: participant.eventInfo,
          name: participant.name,
          sex: participant.gender, // 남성,여성인지 체크
          foreigner: participant.isForeigner ? ["외국인"] : [],
          nationality: participant.nationality,
          idnumber: participant.identityNumber
            ? [idnumber[0], "-", idnumber[1]]
            : [],
          event: event,
          weight: weight,
          editable: false,
        };
      }

      if (mode === 2 || mode === 3) {
        //regist (POST) //modify (PUT)
        let identityNumber = participant.idnumber.join("");
        if (identityNumber === "-") identityNumber = null;

        let eventId = [];
        participant.event.forEach((eventname) => {
          let eventKey = "개인전 " + participant.sex + " " + eventname;
          eventId.push(EVENT_ID[eventKey].id);
        });

        let weightClassId = participant.weight
          ? WEIGHT_ID[participant.sex][participant.weight]
          : null;

        let sendData = {
          name: participant.name.trim(),
          gender: participant.sex,
          isForeigner: participant.foreigner.length > 0 ? true : false,
          nationality: participant.nationality,
          identityNumber: identityNumber,
          phoneNumber: participant.phoneNumber.trim(),
          // eventIds: eventId,
          weightClassId: weightClassId,
        };

        if (mode === 2) {
          return { ...sendData, eventIds: eventId };
        }

        if (mode === 3) {
          //modify (PUT)
          const isNullData = (data) => {
            if (data === null || data === "" || data === undefined) return true;
            else return data;
          };

          let isParticipantChange = false;
          let isEventChange = false;
          let isWeightClassChange = false;
          if (
            sendData.name !== saveParticipant.name ||
            sendData.gender !== saveParticipant.gender ||
            sendData.isForeigner !== saveParticipant.isForeigner ||
            isNullData(sendData.nationality) !==
              isNullData(saveParticipant.nationality) ||
            isNullData(sendData.identityNumber) !==
              isNullData(saveParticipant.identityNumber)
          ) {
            isParticipantChange = true;
          }

          let saveParticipantEventId = [];
          Object.keys(saveParticipant.eventInfo).forEach((event) => {
            saveParticipantEventId.push(Number(event));
          });

          if (
            JSON.stringify(eventId.slice().sort()) !==
            JSON.stringify(saveParticipantEventId.slice().sort())
          ) {
            isEventChange = true;
          }

          let eventInfo = participant.eventInfo;
          if (isEventChange) {
            eventInfo = {};
            //eventId 가 현재 선택한 Id
            eventId.forEach((eId) => {
              if (!saveParticipantEventId.includes(eId)) {
                eventInfo[eId] = null;
              } else {
                eventInfo[eId] = saveParticipant.eventInfo[eId];
              }
            });
          }

          if (
            isNullData(sendData.weightClassId) !==
            isNullData(saveParticipant.weightClassId)
          ) {
            isWeightClassChange = true;
          }

          if (!isParticipantChange && !isEventChange && !isWeightClassChange) {
            return false;
          }

          return {
            ...sendData,
            participantId: participant.participantId,
            participantApplicationId: participant.participantApplicationId,
            isParticipantChange: isParticipantChange,
            isEventChange: isEventChange,
            isWeightClassChange: isWeightClassChange,
            eventInfo: eventInfo,
          };
        }
      }
    },
    []
  );

  const newPersonFormat = useCallback(
    {
      name: "",
      sex: "",
      foreigner: [],
      nationality: "",
      idnumber: ["", "-", ""],
      event: [],
      weight: "",
      isNew: true,
    },
    []
  );

  return (
    <RegistFormat
      koreanTitle="개인전"
      englishTitle="individual"
      personName="선수"
      registTableColumn={TABLE_COLUMNS_REGIST_INDIVIDUAL}
      registTableColumnSecondPeriod={TABLE_COLUMNS_REGIST_PERIOD2_INDIVIDUAL}
      checkTableColumn={TABLE_COLUMNS_CHECK_INDIVIDUAL}
      newPersonFormat={newPersonFormat}
      checkValidity={checkValidityIndividual}
      errMsgPersonName="개인전 선수"
      formatParticipant={formatParticipant}
    />
  );
};

export default RegistIndividual;
