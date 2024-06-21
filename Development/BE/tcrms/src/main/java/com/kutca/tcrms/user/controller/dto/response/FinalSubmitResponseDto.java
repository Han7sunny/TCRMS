package com.kutca.tcrms.user.controller.dto.response;

import com.kutca.tcrms.account.controller.dto.response.AccountResponseDto;
import com.kutca.tcrms.participantapplication.controller.dto.response.ParticipantApplicationsResponseDto;
import lombok.Builder;

public class FinalSubmitResponseDto {

    @Builder
    public static class FirstPeriod {

        private ParticipantApplicationsResponseDto participantApplicationInfos;

        private UserResponseDto userInfo;

        private AccountResponseDto accountInfo;

    }

    @Builder
    public static class SecondPeriod {

        private ParticipantApplicationsResponseDto participantApplicationInfos;

        private boolean isRefundExist;

    }

}
