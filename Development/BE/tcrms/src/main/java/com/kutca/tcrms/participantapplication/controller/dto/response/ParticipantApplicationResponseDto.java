package com.kutca.tcrms.participantapplication.controller.dto.response;

import lombok.Builder;

public class ParticipantApplicationResponseDto {

    @Builder
    public static class firstPeriod {

        private String eventName;

        private int participantCount;

        private int participantFee;

    }
}
