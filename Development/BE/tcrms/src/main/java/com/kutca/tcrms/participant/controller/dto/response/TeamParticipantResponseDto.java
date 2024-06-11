package com.kutca.tcrms.participant.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TeamParticipantResponseDto {

    private int eventTeamNumber;

    private Long eventId;

    private List<TeamMemberParticipantResponseDto> teamMembers;
}
