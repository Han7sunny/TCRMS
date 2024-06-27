package com.kutca.tcrms.user.controller.dto.response;

import com.kutca.tcrms.account.controller.dto.response.AccountResponseDto;
import com.kutca.tcrms.participantapplication.controller.dto.response.ParticipantApplicationResponseDto;
import com.kutca.tcrms.participantapplication.controller.dto.response.ParticipantApplicationsResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

public class FinalSubmitResponseDto {

    @Getter
    @Builder
    public static class FirstPeriod {

        private ParticipantApplicationsResponseDto<ParticipantApplicationResponseDto.FirstPeriod> participantApplicationInfos;

        private UserResponseDto userInfo;

        private AccountResponseDto accountInfo;

    }

    @Getter
    @SuperBuilder
    public static class SecondPeriod {

        private ParticipantApplicationsResponseDto<ParticipantApplicationResponseDto.SecondPeriod> participantApplicationInfos;

        private boolean isRefundExist;

    }

    @Getter
    @Setter
    @SuperBuilder
    public static class Total extends SecondPeriod{

        private AccountResponseDto refundInfo;

    }

}
