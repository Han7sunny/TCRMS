package com.kutca.tcrms.participant.controller.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class IndividualParticipantRequestDto {

    private String name;

    private String gender;

    private Boolean isForeigner;

    private String nationality;

    private String identityNumber;

    private String phoneNumber;

    private List<Long> eventIds;

    private Long weightClassId;
}
