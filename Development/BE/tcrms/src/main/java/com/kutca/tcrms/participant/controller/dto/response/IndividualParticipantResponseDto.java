package com.kutca.tcrms.participant.controller.dto.response;

import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.weightclass.entity.WeightClass;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
@Getter
public class IndividualParticipantResponseDto {

    private Long participantId;

    private String name;

    private String gender;

    private Boolean isForeigner;

    private String nationality;

    private String identityNumber;

    private Map<Long, Long> eventInfo;

    private Long weightClassId;

    public static IndividualParticipantResponseDto fromEntity(Participant participant, List<ParticipantApplication> participantApplicationList) {

        IndividualParticipantResponseDto individualParticipantResponseDto = IndividualParticipantResponseDto.builder()
                .participantId(participant.getParticipantId())
                .name(participant.getName())
                .gender(participant.getGender())
                .isForeigner(participant.getIsForeigner())
                .nationality(participant.getNationality())
                .identityNumber(participant.getIdentityNumber())
                .eventInfo(new HashMap<>())
                .weightClassId(participant.getWeightClass() == null ? null : participant.getWeightClass().getWeightClassId())
                .build();

        participantApplicationList.forEach(participantApplication ->
            individualParticipantResponseDto.eventInfo.put(
                    participantApplication.getEvent().getEventId(), participantApplication.getParticipantApplicationId())
        );

        return individualParticipantResponseDto;
    }

}
