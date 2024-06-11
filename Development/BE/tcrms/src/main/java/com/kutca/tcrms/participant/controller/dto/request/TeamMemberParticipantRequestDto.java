package com.kutca.tcrms.participant.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

public class TeamMemberParticipantRequestDto {

    @Getter
    @Builder
    public static class Regist {

        private String name;

        private String identityNumber;

        private String gender;

        private Boolean isForeigner;

        private String nationality;

        private String phoneNumber;

        private Long weightClassId;

        private String indexInTeam;
    }

    @Getter
    @Builder
    public static class Modify {

        private Long participantId;

        private Boolean isParticipantChange;

        private String name;

        private String identityNumber;

        private String gender;

        private Boolean isForeigner;

        private String nationality;

        private String phoneNumber;

        private Boolean isWeightClassChange;

        private Long weightClassId;

        private Long participantApplicationId;

        private String indexInTeam;
    }
}
