package com.kutca.tcrms.participantapplication.controller.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ParticipantApplicationsRequestDto<T> {

    private List<T> participantApplicationInfos;

}
