package com.kutca.tcrms.participantapplication.controller.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public class ParticipantApplicationsResponseDto<T> {

    private List<T> participantApplicationInfos;
}
