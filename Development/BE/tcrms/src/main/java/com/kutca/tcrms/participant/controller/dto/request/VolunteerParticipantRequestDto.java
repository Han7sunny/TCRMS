package com.kutca.tcrms.participant.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class VolunteerParticipantRequestDto {

    @Getter
    @Builder
    @Schema(name = "VolunteerRegist")
    public static class Regist {

        private String name;

        private String gender;

        private String phoneNumber;

        private Boolean isForeigner;

        private String nationality;
    }

    @Getter
    @Builder
    @Schema(name = "VolunteerModify")
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
    @Schema(name = "VolunteerDelete")
    public static class Delete {

        private Long participantId;

        private Long participantApplicationId;
    }
}
