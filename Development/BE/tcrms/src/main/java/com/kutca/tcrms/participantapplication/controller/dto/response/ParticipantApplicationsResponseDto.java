package com.kutca.tcrms.participantapplication.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ParticipantApplicationsResponseDto<T> {

    private List<T> participantApplicationInfos;

    public ParticipantApplicationsResponseDto(List<T> participantApplicationInfos){
        this.participantApplicationInfos = participantApplicationInfos;
    }
}
