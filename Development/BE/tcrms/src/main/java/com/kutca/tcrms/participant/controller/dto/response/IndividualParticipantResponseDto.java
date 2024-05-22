package com.kutca.tcrms.participant.controller.dto.response;

import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.weightclass.entity.WeightClass;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class IndividualParticipantResponseDto {

    private Long participantId;

    private String participantName;

    private String gender;

    private Boolean isForeigner;

    private String nationality;

    private String identityNumber;

    private List<Long> eventIds;

    private List<Long> participantApplicationIds;

    private Long weightClassId;

    public static IndividualParticipantResponseDto fromEntity(Participant participant, List<ParticipantApplication> participantApplicationList) {

        IndividualParticipantResponseDto individualParticipantResponseDto = IndividualParticipantResponseDto.builder()
                .participantId(participant.getParticipantId())
                .participantName(participant.getName())
                .gender(participant.getGender())
                .isForeigner(participant.getIsForeigner())
                .nationality(participant.getNationality())
                .identityNumber(participant.getIdentityNumber())
                .eventIds(new ArrayList<>())
                .participantApplicationIds(new ArrayList<>())
                .weightClassId(participant.getWeightClass().getWeightClassId())
                .build();

        participantApplicationList.stream().forEach(participantApplication -> {
            individualParticipantResponseDto.eventIds.add(participantApplication.getEvent().getEventId());
            individualParticipantResponseDto.participantApplicationIds.add(participantApplication.getParticipantApplicationId());
        });

        return individualParticipantResponseDto;
    }

}
