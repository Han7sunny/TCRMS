package com.kutca.tcrms.participant.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class TeamParticipantRequestDto {

    @Getter
    @Builder
    @Schema(name = "TeamRegist")
    public static class Regist {

        private Long eventId;

        private List<TeamMemberParticipantRequestDto.Regist> teamMembers;
    }

    @Getter
    @Builder
    @Schema(name = "TeamModify")
    public static class Modify {

        private Long userId;

        private Long eventId;

        private int eventTeamNumber;

        private List<TeamMemberParticipantRequestDto.Modify> teamMembers;
    }

    @Getter
    @Builder
    @Schema(name = "TeamDelete")
    public static class Delete {

        private Long userId;

        private Long eventId;

        private List<Long> participantIds;

        private List<Long> participantApplicationIds;
    }

}
