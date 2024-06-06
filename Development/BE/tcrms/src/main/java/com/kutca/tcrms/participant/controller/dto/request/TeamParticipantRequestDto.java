package com.kutca.tcrms.participant.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class TeamParticipantRequestDto {

    @Getter
    @Builder
    public static class Regist {

        private Long eventId;

        private List<TeamMemberParticipantRequestDto.Regist> teamMembers;
    }

    @Getter
    @Builder
    public static class Modify {

        private Long userId;

        private Long eventId;

        private int eventTeamNumber;

        private List<TeamMemberParticipantRequestDto.Modify> teamMembers;
    }

}
