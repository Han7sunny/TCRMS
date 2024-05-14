package com.kutca.tcrms.participant.controller.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public class ParticipantResponseDto<T> {
    private Boolean isEditable;
    private Boolean isDepositConfirmed;
    private Boolean isParticipantExists;
    private ParticipantsResponseDto participants;
}
