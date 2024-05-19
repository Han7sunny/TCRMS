import React, { useCallback } from "react";

import {
  TABLE_COLUMNS_REGIST_VOLUNTEER,
  TABLE_COLUMNS_CHECK_VOLUNTEER,
} from "../../../shared/util/regist-columns";
import { EVENT_ID } from "../../../shared/util/const-event";
import { checkValidityVolunteer } from "../../../shared/util/regist-validators";

import RegistFormat from "../components/RegistFormat";

const RegistVolunteer = () => {
  const formatParticipant = useCallback((participant, mode) => {
    if (mode === 1) {
      let phoneNumber =
        participant.phoneNumber && participant.phoneNumber.split("-");

      return {
        participantId: participant.participantId,
        name: participant.name,
        sex: participant.gender, // 남성,여성인지 체크
        phoneNumber: participant.phoneNumber
          ? [phoneNumber[0], "-", phoneNumber[1], "-", phoneNumber[2]]
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
        phoneNumber: phoneNumber,
        eventId: EVENT_ID["자원봉사자"],
      };
    }
  }, []);

  const newPersonFormat = useCallback(
    {
      name: "",
      sex: "",
      phoneNumber: ["", "-", "", "-", ""],
    },
    []
  );

  return (
    <RegistFormat
      koreanTitle="자원봉사자"
      englishTitle="volunteer"
      personName="자원봉사자"
      registTableColumn={TABLE_COLUMNS_REGIST_VOLUNTEER}
      checkTableColumn={TABLE_COLUMNS_CHECK_VOLUNTEER}
      newPersonFormat={newPersonFormat}
      checkValidity={checkValidityVolunteer}
      errMsgPersonName="자원봉사자"
      formatParticipant={formatParticipant}
    />
  );
};

export default RegistVolunteer;
