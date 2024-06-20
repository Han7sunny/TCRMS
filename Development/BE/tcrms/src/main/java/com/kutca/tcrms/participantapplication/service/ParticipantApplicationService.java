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
        Map<String, Set<Integer>> teamCount = initializeTeamCount();

        List<Participant> findParticipants = participantRepository.findAllByUser_UserId(userId);
        findParticipants.forEach(participant -> {

            Long participantId = participant.getParticipantId();

            if("여성".equals(participant.getGender())){
                countParticipantApplications(participantId, individualCount, teamCount, 1L, 2L, 5L, 6L);
            }

            if("남성".equals(participant.getGender())){
                countParticipantApplications(participantId, individualCount, teamCount, 3L, 4L, 7L, 8L);
            }

            addParticipantApplicationsToTeamSet(participantId, teamCount.get("품새 페어"), 9L, false);

        });

        return createResponseDto(individualCount, teamCount);

    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getCancelParticipantApplicationFeeInfo(Long userId){

        AtomicInteger individualCount = new AtomicInteger();
        Map<String, Set<Integer>> teamCount = initializeTeamCount();

        List<Participant> findParticipants = participantRepository.findAllByUser_UserId(userId);
        findParticipants.forEach(participant -> {

            Long participantId = participant.getParticipantId();

            if("여성".equals(participant.getGender())){
                countCancelParticipantApplications(participantId, individualCount, teamCount, 1L, 2L, 5L, 6L);
            }

            if("남성".equals(participant.getGender())){
                countCancelParticipantApplications(participantId, individualCount, teamCount, 3L, 4L, 7L, 8L);
            }

            addParticipantApplicationsToTeamSet(participantId, teamCount.get("품새 페어"), 9L, true);

        });

        return createResponseDto(individualCount, teamCount);   //  UniversityApplication 1차 기간 추가해야 함

    }

    private Map<String, Set<Integer>> initializeTeamCount(){
        Map<String, Set<Integer>> teamCount = new HashMap<>();
        teamCount.put("겨루기 단체전", new HashSet<>());
        teamCount.put("품새 단체전", new HashSet<>());
        teamCount.put("품새 페어", new HashSet<>());
        return teamCount;
    }

    private void countParticipantApplications(Long participantId, AtomicInteger individualCount, Map<String, Set<Integer>> teamCount, Long startEventId, Long endEventId, Long sparringEventId, Long poomsaeEventId){
        individualCount.addAndGet(participantApplicationRepository.countAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participantId, startEventId, endEventId));
        addParticipantApplicationsToTeamSet(participantId, teamCount.get("겨루기 단체전"), sparringEventId, false);
        addParticipantApplicationsToTeamSet(participantId, teamCount.get("품새 단체전"), poomsaeEventId, false);
    }

    private void countCancelParticipantApplications(Long participantId, AtomicInteger individualCount, Map<String, Set<Integer>> teamCount, Long startEventId, Long endEventId, Long sparringEventId, Long poomsaeEventId){
        individualCount.addAndGet(participantApplicationRepository.countAllByParticipant_ParticipantIdAndEvent_EventIdBetweenAndIs2ndCancelTrue(participantId, startEventId, endEventId));
        addParticipantApplicationsToTeamSet(participantId, teamCount.get("겨루기 단체전"), sparringEventId, true);
        addParticipantApplicationsToTeamSet(participantId, teamCount.get("품새 단체전"), poomsaeEventId, true);
    }

    private void addParticipantApplicationsToTeamSet(Long participantId, Set<Integer> teamSet, Long eventId, boolean isCancel){
        List<ParticipantApplication> findParticipantApplications;
        if(isCancel){
            findParticipantApplications = participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventIdAndIs2ndCancelTrue(participantId, eventId);
        }
        else {
            findParticipantApplications = participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(participantId, eventId);
        }
        findParticipantApplications.forEach(participantApplication -> teamSet.add(participantApplication.getEventTeamNumber()));
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
