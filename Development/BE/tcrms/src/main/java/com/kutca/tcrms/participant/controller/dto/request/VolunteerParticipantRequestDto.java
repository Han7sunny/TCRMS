package com.kutca.tcrms.participant.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

public class VolunteerParticipantRequestDto {

    @Getter
    @Builder
    public static class Regist {

        private String name;

        private String gender;

        private String phoneNumber;

        private Boolean isForeigner;

        private String nationality;
    }

    @Getter
    @Builder
    public static class Modify {

        private Long participantId;

        private String name;

        private String gender;

        private String phoneNumber;

        private Boolean isForeigner;

        private String nationality;
    }

    @Getter
    @Builder
    public static class Delete {

        private Long participantId;

        private Long participantApplicationId;
    }
}
