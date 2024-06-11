package com.kutca.tcrms.participant.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamMemberParticipantResponseDto {

    private Long participantApplicationId;

    private Long participantId;

    private Long weightClassId;

    private String name;

    private String identityNumber;

    private String gender;

    private Boolean isForeigner;

    private String nationality;

    private String phoneNumber;

    private String indexInTeam;

}
