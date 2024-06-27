package com.kutca.tcrms.participant.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class TeamMemberParticipantRequestDto {

    @Getter
    @Builder
    @Schema(name = "TeamMemberRegist")
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
    @Schema(name = "TeamMemberModify")
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
