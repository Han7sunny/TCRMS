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
    }

    @Getter
    public static class Modify {

        private Long participantId;

        private String name;

        private String gender;

        private String phoneNumber;
    }

    @Getter
    public static class Delete {

        private Long participantId;

        private Long participantApplicationId;
    }
}
