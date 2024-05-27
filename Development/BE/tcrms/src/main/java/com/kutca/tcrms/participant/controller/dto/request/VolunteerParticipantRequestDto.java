package com.kutca.tcrms.participant.controller.dto.request;

import lombok.Getter;

public class VolunteerParticipantRequestDto {

    @Getter
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
}
