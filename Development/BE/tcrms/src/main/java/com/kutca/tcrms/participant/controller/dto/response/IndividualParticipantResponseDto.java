package com.kutca.tcrms.participant.controller.dto.response;

import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.weightclass.entity.WeightClass;
import lombok.Builder;
import lombok.Getter;

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

    private Long weightClassId;

    public static IndividualParticipantResponseDto fromEntity(Participant participant, Long weightClassId, List<Event> eventList) {
        return IndividualParticipantResponseDto.builder()
                .participantId(participant.getParticipantId())
                .participantName(participant.getName())
                .gender(participant.getGender())
                .isForeigner(participant.getIsForeigner())
                .nationality(participant.getNationality())
                .identityNumber(participant.getIdentityNumber())
                .eventIds(eventList.stream().map(Event::getEventId).collect(Collectors.toList()))
                .weightClassId(weightClassId)
                .build();
    }

}
