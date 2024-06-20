package com.kutca.tcrms.participantapplication.service;

import com.kutca.tcrms.account.controller.dto.response.AccountResponseDto;
import com.kutca.tcrms.account.entity.Account;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.common.enums.DatePeriod;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.IndividualParticipantRequestDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participantapplication.controller.dto.response.ParticipantApplicationResponseDto;
import com.kutca.tcrms.participantapplication.controller.dto.response.ParticipantApplicationsResponseDto;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.secondperiod.entity.SecondPeriod;
import com.kutca.tcrms.secondperiod.repository.SecondPeriodRepository;
import com.kutca.tcrms.universityapplication.entity.UniversityApplication;
import com.kutca.tcrms.universityapplication.repository.UniversityApplicationRepository;
import com.kutca.tcrms.user.controller.dto.request.FinalSubmitRequestDto;
import com.kutca.tcrms.user.controller.dto.response.FinalSubmitResponseDto;
import com.kutca.tcrms.user.controller.dto.response.UserResponseDto;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ParticipantApplicationService {

    private final UserRepository userRepository;

    private final ParticipantApplicationRepository participantApplicationRepository;
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;

    private final UniversityApplicationRepository universityApplicationRepository;

//    @Value("${kutca.admin.id}")
    private static final Long KUTCA_ID = 1L;   //  추후 application.properties에서 값 추출

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

    private final SecondPeriodRepository secondPeriodRepository;

//    @Transactional(readOnly = true)
    public AccountResponseDto getDepositAccountInfo(Long userId){
        //  userId에 해당하는 SecondPeriod 데이터 없을 경우 존재?
        Account depositAccount = secondPeriodRepository.findByUser_UserId(userId).get().getAccount();

        return AccountResponseDto.builder()
                        .accountBank(depositAccount.getAccountBank())
                        .accountNumber(depositAccount.getAccountNumber())
                        .depositOwnerName(depositAccount.getDepositOwnerName())
                .build();
    }

    public UserResponseDto getUserInfo(Long userId){

        User user = userRepository.findById(userId).get();
        return UserResponseDto.builder()
                .userName(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .universityName(user.getUniversityName())
                .depositorName(user.getDepositorName())
                .build();
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getFirstPeriodParticipantApplicationFeeInfo(Long userId){

        //  1차 참가비 및 임금, 대표자 정보 조회 (최종 제출 전)

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

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(
                        FinalSubmitResponseDto.FirstPeriod.builder()
                        .participantApplicationInfos((ParticipantApplicationsResponseDto) getParticipantApplicationInfos(individualCount, teamCount))
                        .userInfo(getUserInfo(userId))
                        .accountInfo(getDepositAccountInfo(KUTCA_ID))
                        .build())
                .build();

    }

    public ResponseDto<?> finalSubmitInFirstPeriod(FinalSubmitRequestDto.FirstPeriod firstPeriodFinalSubmitRequestDto){

        Optional<User> findUser = userRepository.findById(firstPeriodFinalSubmitRequestDto.getUserInfo().getUserId());
        if(findUser.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("대표자 정보를 조회할 수 없습니다.")
                    .build();
        }

        User user = findUser.get();
        user.updatePhoneNumberAndDepositorName(firstPeriodFinalSubmitRequestDto.getUserInfo().getPhoneNumber(), firstPeriodFinalSubmitRequestDto.getUserInfo().getDepositorName());
        userRepository.save(user);

        firstPeriodFinalSubmitRequestDto.getParticipantApplicationInfos().getParticipantApplicationInfos().forEach(participantApplication ->
            universityApplicationRepository.save(
                    UniversityApplication.builder()
                            .user(user)
                            .eventName(participantApplication.getEventName())
                            .teamCount(participantApplication.getParticipantCount())
                            .teamFee(participantApplication.getParticipantFee())
                            .period(DatePeriod.FIRST.name())
                            .build()

            )
        );

        return ResponseDto.builder()
                .isSuccess(true)
                .message("1차 최종 제출이 성공적으로 완료되었습니다.")
                .build();

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

    private ParticipantApplicationResponseDto.FirstPeriod createParticipantApplicationInfo(String eventName, int participantCount, Long eventId){

        int participantFee = eventRepository.findById(eventId).get().getEventFee();

        return ParticipantApplicationResponseDto.FirstPeriod.builder()
                .eventName(eventName)
                .participantCount(participantCount)
                .participantFee(participantCount * participantFee)
                .build();

    }

    private List<ParticipantApplicationResponseDto.FirstPeriod> getParticipantApplicationInfos(AtomicInteger individualCount, Map<String, Set<Integer>> teamCount){
        return Arrays.asList(
                createParticipantApplicationInfo("개인전", individualCount.get(), 1L),
                createParticipantApplicationInfo("겨루기 단체전", teamCount.get("겨루기 단체전").size(), 5L),
                createParticipantApplicationInfo("품새 단체전", teamCount.get("품새 단체전").size(), 6L),
                createParticipantApplicationInfo("품새 페어", teamCount.get("품새 페어").size(), 9L)
        );
    }

    private ResponseDto<?> createResponseDto(AtomicInteger individualCount, Map<String, Set<Integer>> teamCount){

        List<ParticipantApplicationResponseDto.FirstPeriod> participantApplicationInfos = Arrays.asList(
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
