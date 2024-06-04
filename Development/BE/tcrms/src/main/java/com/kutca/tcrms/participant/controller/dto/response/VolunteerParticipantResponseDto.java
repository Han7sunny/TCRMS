package com.kutca.tcrms.participant.controller.dto.response;

import com.kutca.tcrms.participant.entity.Participant;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VolunteerParticipantResponseDto {

    private Long participantId;

    private String name;

    private String gender;

    private String phoneNumber;

    public static VolunteerParticipantResponseDto fromEntity(Participant participant){
        return VolunteerParticipantResponseDto.builder()
                .participantId(participant.getParticipantId())
                .name(participant.getName())
                .gender(participant.getGender())
                .phoneNumber(participant.getPhoneNumber())
                .build();
    }
}
