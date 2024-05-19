import React, { useCallback } from "react";

import {
  TABLE_COLUMNS_REGIST_INDIVIDUAL,
  TABLE_COLUMNS_CHECK_INDIVIDUAL,
} from "../../../shared/util/regist-columns";
import { EVENT_ID, WEIGHT_ID } from "../../../shared/util/const-event";
import { checkValidityIndividual } from "../../../shared/util/regist-validators";

import RegistFormat from "../components/RegistFormat";

const RegistIndividual = () => {
  const formatParticipant = useCallback((participant, mode) => {
    if (mode === 1) {
      let event = new Set();
      if (participant.eventId) {
        participant.eventId.forEach((eventId) => {
          let eventName = Object.keys(EVENT_ID).find(
            (key) => EVENT_ID[key] === eventId
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

    if (mode === 2) {
      let identityNumber = participant.idnumber.join("");
      if (identityNumber === "-") identityNumber = null;

      let eventId = [];
      participant.event.forEach((eventname) => {
        let eventKey = "개인전 " + participant.sex + " " + eventname;
        eventId.push(EVENT_ID[eventKey]);
      });

      let weightClassId = participant.weight
        ? WEIGHT_ID[participant.sex][participant.weight]
        : null;

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
  }, []);

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
      checkTableColumn={TABLE_COLUMNS_CHECK_INDIVIDUAL}
      newPersonFormat={newPersonFormat}
      checkValidity={checkValidityIndividual}
      errMsgPersonName="개인전 선수"
      formatParticipant={formatParticipant}
    />
  );
};

export default RegistIndividual;
