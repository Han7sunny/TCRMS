package com.kutca.tcrms.participant.controller.dto.request;

import lombok.Getter;

import java.util.List;


public class IndividualParticipantRequestDto {

    @Getter
    public static class Regist {

        private String name;

        private String gender;

        private Boolean isForeigner;

        private String nationality;

        private String identityNumber;

        private String phoneNumber;

        private List<Long> eventIds;

        private Long weightClassId;
    }

    @Getter
    public static class Delete {

        private Long participantId;

        private List<Long> participantApplicationIds;
    }

}
