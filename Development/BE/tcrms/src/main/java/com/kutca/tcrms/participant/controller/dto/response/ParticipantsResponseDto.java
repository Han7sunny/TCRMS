package com.kutca.tcrms.participant.controller.dto.response;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsResponseDto<T> {
    private final List<T> participants;

    public ParticipantsResponseDto() {
        this.participants = new ArrayList<>();
    }

}
