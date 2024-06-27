package com.kutca.tcrms.participant.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class SecondParticipantRequestDto {

    @Getter
    @Builder
    @Schema(name = "SecondRegist")
    public static class Regist {

        private String name;

        private String gender;

        private Boolean isForeigner;

        private String nationality;

        private String identityNumber;

        private String phoneNumber;

    }

    @Getter
    @Builder
    @Schema(name = "SecondModify")
    public static class Modify {

        private Long participantId;

        private Long participantApplicationId;

        private String name;

        private String gender;

        private Boolean isForeigner;

        private String nationality;

        private String identityNumber;

        private String phoneNumber;
    }

    @Getter
    @Builder
    @Schema(name = "SecondDelete")
    public static class Delete {

        private Long participantId;

        private Long participantApplicationId;

    }
}
