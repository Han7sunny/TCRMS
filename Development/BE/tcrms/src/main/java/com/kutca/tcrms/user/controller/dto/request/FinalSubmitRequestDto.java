package com.kutca.tcrms.user.controller.dto.request;

import com.kutca.tcrms.account.controller.dto.request.AccountRequestDto;
import com.kutca.tcrms.participantapplication.controller.dto.request.ParticipantApplicationRequestDto;
import com.kutca.tcrms.participantapplication.controller.dto.request.ParticipantApplicationsRequestDto;
import lombok.Getter;

public class FinalSubmitRequestDto {

    @Getter
    public static class FirstPeriod {

        private UserRequestDto.Info userInfo;

        private ParticipantApplicationsRequestDto<ParticipantApplicationRequestDto.FirstPeriod> participantApplicationInfos;

    }

    @Getter
    public static class SecondPeriod {

        private AccountRequestDto accountInfo;

        private ParticipantApplicationsRequestDto<ParticipantApplicationRequestDto.SecondPeriod> participantApplicationInfos;

    }

}
