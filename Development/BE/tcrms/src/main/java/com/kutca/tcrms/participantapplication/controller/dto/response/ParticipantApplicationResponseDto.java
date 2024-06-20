package com.kutca.tcrms.participantapplication.controller.dto.response;

import lombok.Builder;

public class ParticipantApplicationResponseDto {

    @Builder
    public static class FirstPeriod {

        private String eventName;

        private int participantCount;

        private int participantFee;

    }

    @Builder
    public static class SecondPeriod extends FirstPeriod {

        private int cancelParticipantCount;

        private int refundParticipantFee;

    }
}
