package com.kutca.tcrms.user.controller.dto.request;

import com.kutca.tcrms.participantapplication.controller.dto.request.ParticipantApplicationsRequestDto;
import lombok.Getter;

public class FinalSubmitRequestDto {

    @Getter
    public static class FirstPeriod {

        private UserRequestDto.Info userInfo;

        private ParticipantApplicationsRequestDto participantApplicationInfos;

    }

}
