package com.kutca.tcrms.participant.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ParticipantResponseDto<T> {
    private Boolean isEditable;
    private Boolean isDepositConfirmed;
    private Boolean isParticipantExists;
    private List<T> participants;
}
