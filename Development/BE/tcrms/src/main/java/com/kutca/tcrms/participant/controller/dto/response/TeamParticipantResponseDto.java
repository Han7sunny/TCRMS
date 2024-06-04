package com.kutca.tcrms.participant.controller.dto.response;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class TeamParticipantResponseDto {

    private int eventTeamNumber;

    private Long eventId;

    private List<TeamMemberParticipantResponseDto> teamMembers;
}
