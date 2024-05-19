import React, { useCallback } from "react";

import {
  TABLE_COLUMNS_REGIST_SECOND,
  TABLE_COLUMNS_CHECK_SECOND,
} from "../../../shared/util/regist-columns";
import { EVENT_ID } from "../../../shared/util/const-event";
import { checkValiditySecond } from "../../../shared/util/regist-validators";

import RegistFormat from "../components/RegistFormat";

const RegistSecond = () => {
  const formatParticipant = useCallback((participant, mode) => {
    if (mode === 1) {
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
  }, []);

  const newPersonFormat = useCallback(
    {
      name: "",
      sex: "",
      foreigner: [],
      nationality: "",
      idnumber: ["", "-", ""],
    },
    []
  );

  return (
    <RegistFormat
      koreanTitle="세컨"
      englishTitle="second"
      personName="세컨"
      registTableColumn={TABLE_COLUMNS_REGIST_SECOND}
      checkTableColumn={TABLE_COLUMNS_CHECK_SECOND}
      newPersonFormat={newPersonFormat}
      checkValidity={checkValiditySecond}
      errMsgPersonName="세컨"
      formatParticipant={formatParticipant}
    />
  );
};

export default RegistSecond;
