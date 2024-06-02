package com.kutca.tcrms.participant.controller.dto.response;

import com.kutca.tcrms.participant.entity.Participant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SecondParticipantResponseDto {

    private Long participantId;

    private String name;

    private String gender;

    private String identityNumber;

    private Boolean isForeigner;

    private String nationality;

    private String phoneNumber;

    public static SecondParticipantResponseDto fromEntity(Participant participant) {
        return SecondParticipantResponseDto.builder()
                .participantId(participant.getParticipantId())
                .name(participant.getName())
                .gender(participant.getGender())
                .identityNumber(participant.getIdentityNumber() == null ? null : participant.getIdentityNumber())
                .isForeigner(participant.getIsForeigner())
                .nationality(participant.getNationality())
                .phoneNumber(participant.getPhoneNumber() == null ? null : participant.getPhoneNumber())
                .build();
    }
}
