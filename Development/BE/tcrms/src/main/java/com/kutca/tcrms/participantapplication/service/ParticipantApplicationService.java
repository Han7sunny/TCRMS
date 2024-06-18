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

        //  개인전(1~4), 겨루기 단체전(5,7), 품새 단체전(6,8), 품새 페어(9)

        AtomicInteger individualCount = new AtomicInteger();
        Map<String, Set<Integer>> teamCount = new HashMap<>();
        teamCount.put("겨루기 단체전", new HashSet<>());
        teamCount.put("품새 단체전", new HashSet<>());
        teamCount.put("품새 페어", new HashSet<>());

        List<Participant> findParticipants = participantRepository.findAllByUser_UserId(userId);
        findParticipants.forEach(participant -> {

            Long participantId = participant.getParticipantId();

            if(participant.getGender().equals("여성")){

                //  개인전
                individualCount.addAndGet(participantApplicationRepository.countAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participantId, 1L, 2L));

                //  겨루기 단체전
                List<ParticipantApplication> findParticipantApplications1 = participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(participantId, 5L);
                for (ParticipantApplication participantApplication : findParticipantApplications1) {
                    teamCount.get("겨루기 단체전").add(participantApplication.getEventTeamNumber());
                }
                
                //  품새 단체전
                List<ParticipantApplication> findParticipantApplications2 = participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(participantId, 6L);
                for (ParticipantApplication participantApplication : findParticipantApplications2) {
                    teamCount.get("품새 단체전").add(participantApplication.getEventTeamNumber());
                }
            }

            if(participant.getGender().equals("남성")){

                //  개인전
                individualCount.addAndGet(participantApplicationRepository.countAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participantId, 3L, 4L));

                //  겨루기 단체전
                List<ParticipantApplication> findParticipantApplications1 = participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(participantId, 7L);
                for (ParticipantApplication participantApplication : findParticipantApplications1) {
                    teamCount.get("겨루기 단체전").add(participantApplication.getEventTeamNumber());
                }

                //  품새 단체전
                List<ParticipantApplication> findParticipantApplications2 = participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(participantId, 8L);
                for (ParticipantApplication participantApplication : findParticipantApplications2) {
                    teamCount.get("품새 단체전").add(participantApplication.getEventTeamNumber());
                }

            }

            //  품새 페어
            List<ParticipantApplication> findParticipantApplications = participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(participantId, 9L);
            for (ParticipantApplication participantApplication : findParticipantApplications) {
                teamCount.get("품새 페어").add(participantApplication.getEventTeamNumber());
            }

        });

        ParticipantApplicationResponseDto.firstPeriod participantApplicationInfo1 = ParticipantApplicationResponseDto.firstPeriod.builder()
                .eventName("개인전")
                .participantCount(individualCount.get())
                .participantFee(individualCount.get() * eventRepository.findById(1L).get().getEventFee())
                .build();

        ParticipantApplicationResponseDto.firstPeriod participantApplicationInfo2 = ParticipantApplicationResponseDto.firstPeriod.builder()
                .eventName("겨루기 단체전")
                .participantCount(teamCount.get("겨루기 단체전").size())
                .participantFee(teamCount.get("겨루기 단체전").size() * eventRepository.findById(5L).get().getEventFee())
                .build();

        ParticipantApplicationResponseDto.firstPeriod participantApplicationInfo3 = ParticipantApplicationResponseDto.firstPeriod.builder()
                .eventName("품새 단체전")
                .participantCount(teamCount.get("품새 단체전").size())
                .participantFee(teamCount.get("품새 단체전").size() * eventRepository.findById(6L).get().getEventFee())
                .build();

        ParticipantApplicationResponseDto.firstPeriod participantApplicationInfo4 = ParticipantApplicationResponseDto.firstPeriod.builder()
                .eventName("품새 페어")
                .participantCount(teamCount.get("품새 페어").size())
                .participantFee(teamCount.get("품새 페어").size() * eventRepository.findById(9L).get().getEventFee())
                .build();

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(
                        ParticipantApplicationsResponseDto.builder()
                        .participantApplicationInfos(Arrays.asList(participantApplicationInfo1, participantApplicationInfo2, participantApplicationInfo3, participantApplicationInfo4))
                        .build())
                .build();
    }
}
