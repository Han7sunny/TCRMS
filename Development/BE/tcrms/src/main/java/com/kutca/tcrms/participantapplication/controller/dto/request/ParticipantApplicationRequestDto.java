package com.kutca.tcrms.participantapplication.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ParticipantApplicationRequestDto {

    private String eventName;

    private int participantCount;

    private int participantFee;

}
