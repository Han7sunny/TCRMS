package com.kutca.tcrms.participant.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

public class TeamMemberParticipantRequestDto {

    @Getter
    @Builder
    public static class Regist {

        private String name;

        private String identityName;

        private String gender;

        private Boolean isForeigner;

        private String nationality;

        private String phoneNumber;

        private Long weightClassId;

        private String indexInTeam;
    }
}
