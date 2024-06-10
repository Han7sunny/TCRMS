import React, { useCallback } from "react";

import {
  TABLE_COLUMNS_REGIST_VOLUNTEER,
  TABLE_COLUMNS_REGIST_PERIOD2_VOLUNTEER,
  TABLE_COLUMNS_CHECK_VOLUNTEER,
} from "../../../shared/util/regist/regist-columns";
// import { EVENT_ID } from "../../../shared/util/const-event";
import { checkValidityVolunteer } from "../../../shared/util/regist/regist-validators";

import RegistFormat from "../components/RegistFormat";

const RegistVolunteer = () => {
  const formatParticipant = useCallback(
    (participant, mode, saveParticipant) => {
      if (mode === 1) {
        let phoneNumber =
          participant.phoneNumber && participant.phoneNumber.split("-");

        return {
          participantId: participant.participantId,
          eventInfo: participant.eventInfo,
          name: participant.name,
          sex: participant.gender, // 남성,여성인지 체크
          phoneNumber: participant.phoneNumber
            ? [phoneNumber[0], "-", phoneNumber[1], "-", phoneNumber[2]]
            : [],
        };
      }

      if (mode === 2 || mode === 3) {
        //regist (POST) //modify (PUT)
        let phoneNumber = participant.phoneNumber.join("");
        if (phoneNumber === "--") phoneNumber = null;

        let sendData = {
          name: participant.name.trim(),
          gender: participant.sex,
          phoneNumber: phoneNumber,
          // eventId: EVENT_ID["자원봉사자"],
        };

        if (mode === 2) {
          return sendData;
        }

        if (mode === 3) {
          //modify (PUT)
          const isNullData = (data) => {
            if (data === null || data === "" || data === undefined) return true;
            else return data;
          };

          let isParticipantChange = false;
          if (
            sendData.name !== saveParticipant.name ||
            sendData.gender !== saveParticipant.gender ||
            isNullData(sendData.phoneNumber) !==
              isNullData(saveParticipant.phoneNumber)
          ) {
            isParticipantChange = true;
          }

          if (!isParticipantChange) {
            return false;
          }

          return {
            ...sendData,
            participantId: participant.participantId,
            // isParticipantChange: isParticipantChange,
            // eventInfo: eventInfo,
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
      phoneNumber: ["", "-", "", "-", ""],
      isNew: true,
    },
    []
  );

  return (
    <RegistFormat
      koreanTitle="자원봉사자"
      englishTitle="volunteer"
      personName="자원봉사자"
      registTableColumn={TABLE_COLUMNS_REGIST_VOLUNTEER}
      registTableColumnSecondPeriod={TABLE_COLUMNS_REGIST_PERIOD2_VOLUNTEER}
      checkTableColumn={TABLE_COLUMNS_CHECK_VOLUNTEER}
      newPersonFormat={newPersonFormat}
      checkValidity={checkValidityVolunteer}
      errMsgPersonName="자원봉사자"
      formatParticipant={formatParticipant}
    />
  );
};

export default RegistVolunteer;
