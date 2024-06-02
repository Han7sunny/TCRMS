import React, { useCallback } from "react";

import {
  TABLE_COLUMNS_REGIST_SECOND,
  TABLE_COLUMNS_REGIST_PERIOD2_SECOND,
  TABLE_COLUMNS_CHECK_SECOND,
} from "../../../shared/util/regist-columns";
// import { EVENT_ID } from "../../../shared/util/const-event";
import { checkValiditySecond } from "../../../shared/util/regist-validators";

import RegistFormat from "../components/RegistFormat";

const RegistSecond = () => {
  const formatParticipant = useCallback(
    (participant, mode, saveParticipant) => {
      if (mode === 1) {
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
          phoneNumber: participant.phoneNumber,
        };
      }

      if (mode === 2 || mode === 3) {
        //regist (POST) //modify (PUT)
        let identityNumber = participant.idnumber.join("");
        if (identityNumber === "-") identityNumber = null;

        let sendData = {
          name: participant.name.trim(),
          gender: participant.sex,
          isForeigner: participant.foreigner.length > 0 ? true : false,
          nationality: participant.nationality,
          identityNumber: identityNumber,
          phoneNumber: participant.phoneNumber.trim(),
          // eventId: EVENT_ID["세컨"],
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
            sendData.isForeigner !== saveParticipant.isForeigner ||
            isNullData(sendData.nationality) !==
              isNullData(saveParticipant.nationality) ||
            isNullData(sendData.identityNumber) !==
              isNullData(saveParticipant.identityNumber) ||
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
      foreigner: [],
      nationality: "",
      idnumber: ["", "-", ""],
      phoneNumber: "",
      isNew: true,
    },
    []
  );

  return (
    <RegistFormat
      koreanTitle="세컨"
      englishTitle="second"
      personName="세컨"
      registTableColumn={TABLE_COLUMNS_REGIST_SECOND}
      registTableColumnSecondPeriod={TABLE_COLUMNS_REGIST_PERIOD2_SECOND}
      checkTableColumn={TABLE_COLUMNS_CHECK_SECOND}
      newPersonFormat={newPersonFormat}
      checkValidity={checkValiditySecond}
      errMsgPersonName="세컨"
      formatParticipant={formatParticipant}
    />
  );
};

export default RegistSecond;
