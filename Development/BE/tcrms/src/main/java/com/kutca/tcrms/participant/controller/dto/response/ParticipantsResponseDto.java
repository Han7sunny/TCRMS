package com.kutca.tcrms.participant.controller.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ParticipantsResponseDto<T> {
    private final List<T> participants;

    public ParticipantsResponseDto(List<T> participants) {
        this.participants = participants;
    }

}
