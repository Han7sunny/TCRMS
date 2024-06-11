package com.kutca.tcrms.participant.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ParticipantsResponseDto<T> {
    private List<T> participants;

    public ParticipantsResponseDto(List<T> participants) {
        this.participants = participants;
    }

}
