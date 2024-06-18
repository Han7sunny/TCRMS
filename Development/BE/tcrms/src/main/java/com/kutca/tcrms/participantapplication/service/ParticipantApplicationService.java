package com.kutca.tcrms.participantapplication.service;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.IndividualParticipantRequestDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participantapplication.controller.dto.response.ParticipantApplicationResponseDto;
import com.kutca.tcrms.participantapplication.controller.dto.response.ParticipantApplicationsResponseDto;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ParticipantApplicationService {

    private final ParticipantApplicationRepository participantApplicationRepository;
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;

    public ResponseDto<?> deleteParticipantApplication(IndividualParticipantRequestDto.Delete individualParticipantRequestDto){

        individualParticipantRequestDto.getParticipantApplicationIds().forEach(participantApplicationId -> {
            participantApplicationRepository.deleteById(participantApplicationId);
            //  학교별 신청 종목 팀 데이터 변경
                //  2차 수정 및 취소 기간
        });

        Boolean isOtherParticipantApplicationExist = participantApplicationRepository.existsByParticipant_ParticipantId(individualParticipantRequestDto.getParticipantId());

        if(!isOtherParticipantApplicationExist){
            participantRepository.deleteById(individualParticipantRequestDto.getParticipantId());
            //  participant_file
            //  file
        }

        return ResponseDto.builder()
                .isSuccess(true)
                .build();
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getParticipantApplicationFeeInfo(Long userId){

        //  개인전(1 ~ 4), 겨루기 단체전(5,7), 품새 단체전(6,8), 품새 페어(9)

        AtomicInteger individualCount = new AtomicInteger();
        Map<String, Set<Integer>> teamCount = new HashMap<>();
        teamCount.put("겨루기 단체전", new HashSet<>());
        teamCount.put("품새 단체전", new HashSet<>());
        teamCount.put("품새 페어", new HashSet<>());

        List<Participant> findParticipants = participantRepository.findAllByUser_UserId(userId);
        findParticipants.forEach(participant -> {

            Long participantId = participant.getParticipantId();

            if("여성".equals(participant.getGender())){
                countIndividualParticipantApplications(participantId, individualCount, 1L, 2L);
                countTeamParticipantApplications(participantId, teamCount, 5L, 6L);
            }

            if("남성".equals(participant.getGender())){
                countIndividualParticipantApplications(participantId, individualCount, 3L, 4L);
                countTeamParticipantApplications(participantId, teamCount, 7L, 8L);
            }

            List<ParticipantApplication> findParticipantApplications = participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(participantId, 9L);
            findParticipantApplications.forEach(participantApplication -> teamCount.get("품새 페어").add(participantApplication.getEventTeamNumber()));

        });

        return createResponseDto(individualCount, teamCount);

    }

    private void countIndividualParticipantApplications(Long participantId, AtomicInteger individualCount, Long startEventId, Long endEventId){
        individualCount.addAndGet(participantApplicationRepository.countAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participantId, startEventId, endEventId));
    }

    private void addTeamParticipantApplicationNumbers(Long participantId, Set<Integer> teamSet, Long eventId){
        List<ParticipantApplication> findParticipantApplications = participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(participantId, eventId);
        findParticipantApplications.forEach(participantApplication -> teamSet.add(participantApplication.getEventTeamNumber()));
    }

    private void countTeamParticipantApplications(Long participantId, Map<String, Set<Integer>> teamCount, Long sparringEventId, Long poomsaeEventId){
        addTeamParticipantApplicationNumbers(participantId, teamCount.get("겨루기 단체전"), sparringEventId);
        addTeamParticipantApplicationNumbers(participantId, teamCount.get("품새 단체전"), poomsaeEventId);
    }

    private ParticipantApplicationResponseDto.firstPeriod createParticipantApplicationInfo(String eventName, int participantCount, Long eventId){

        int participantFee = eventRepository.findById(eventId).get().getEventFee();

        return ParticipantApplicationResponseDto.firstPeriod.builder()
                .eventName(eventName)
                .participantCount(participantCount)
                .participantFee(participantCount * participantFee)
                .build();

    }

    private ResponseDto<?> createResponseDto(AtomicInteger individualCount, Map<String, Set<Integer>> teamCount){

        List<ParticipantApplicationResponseDto.firstPeriod> participantApplicationInfos = Arrays.asList(
                createParticipantApplicationInfo("개인전", individualCount.get(), 1L),
                createParticipantApplicationInfo("겨루기 단체전", teamCount.get("겨루기 단체전").size(), 5L),
                createParticipantApplicationInfo("품새 단체전", teamCount.get("품새 단체전").size(), 6L),
                createParticipantApplicationInfo("품새 페어", teamCount.get("품새 페어").size(), 9L)
                );

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(
                        ParticipantApplicationsResponseDto.builder()
                                .participantApplicationInfos(Collections.singletonList(participantApplicationInfos))
                                .build())
                .build();
    }
}
