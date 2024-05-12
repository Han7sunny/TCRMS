package com.kutca.tcrms.participant.controller.dto.response;

import com.kutca.tcrms.participant.entity.Participant;
import lombok.Builder;

@Builder
public class IndividualParticipantResponseDto {

    private Long participantId;

    private String participantName;

    private String gender;

    private Boolean isForeigner;

    private String nationality;

    private String identityNumber;

    private Long eventId;

    private Long weightClassId;

    public static IndividualParticipantResponseDto fromEntity(Participant participant) {
        return IndividualParticipantResponseDto.builder()
                .participantId(participant.getParticipantId())
                .participantName(participant.getName())
                .gender(participant.getGender())
                .isForeigner(participant.getIsForeigner())
                .nationality(participant.getNationality())
                .identityNumber(participant.getIdentityNumber())
//                .eventId(participant.eventId)
//                .weightClassId(participant.getWeightClass())
                .build();
    }

}
