package com.kutca.tcrms.participantapplication.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ParticipantApplicationRequestDto {

    @Getter
    public static class FirstPeriod {

        private String eventName;

        private int participantCount;

        private int participantFee;

    }

    @Getter
    public static class SecondPeriod {

        private String eventName;

        private int cancelParticipantCount;

        private int refundParticipantFee;

    }

}
