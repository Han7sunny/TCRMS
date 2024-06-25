package com.kutca.tcrms.participantapplication.service;

import com.kutca.tcrms.account.controller.dto.response.AccountResponseDto;
import com.kutca.tcrms.account.entity.Account;
import com.kutca.tcrms.account.repository.AccountRepository;
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
import com.kutca.tcrms.universityapplication.controller.dto.response.UniversityApplicationResponseDto;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantApplicationService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final AccountRepository accountRepository;
    private final SecondPeriodRepository secondPeriodRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantApplicationRepository participantApplicationRepository;
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
        if(findParticipants.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(true)
                    .message("신청 내역이 존재하지 않습니다.")
                    .build();
        }

        findParticipants.forEach(participant -> {

            Long participantId = participant.getParticipantId();

            if("여성".equals(participant.getGender())){
                countParticipantApplications(participantId, participant.getGender(), individualCount, teamCount, 1L, 2L, 5L, 6L);
            }

            if("남성".equals(participant.getGender())){
                countParticipantApplications(participantId, participant.getGender(), individualCount, teamCount, 3L, 4L, 7L, 8L);
            }

            addParticipantApplicationsToTeamSet(participantId, teamCount.get("품새 페어"), 9L, false, participant.getGender());

        });

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(
                        FinalSubmitResponseDto.FirstPeriod.builder()
                        .participantApplicationInfos(new ParticipantApplicationsResponseDto<>(getParticipantApplicationInfos(individualCount, teamCount)))
                        .userInfo(getUserInfo(userId))
                        .accountInfo(getDepositAccountInfo(KUTCA_ID))
                        .build())
                .build();

    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getSecondPeriodParticipantApplicationFeeInfo(Long userId){

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

            addParticipantApplicationsToTeamSet(participantId, teamCount.get("품새 페어"), 9L, true, participant.getGender());

        });

        List<UniversityApplicationResponseDto.FirstPeriod> universityApplicationInfos = getParticipantApplicationInfoFromUniversityApplication(userId, DatePeriod.FIRST.name());
        List<ParticipantApplicationResponseDto.SecondPeriod> participantApplicationInfos = getParticipantApplicationInfosInSecondPeriod(universityApplicationInfos, individualCount, teamCount);

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(
                        FinalSubmitResponseDto.SecondPeriod.builder()
                                .participantApplicationInfos(new ParticipantApplicationsResponseDto<>(participantApplicationInfos))
                                .isRefundExist(!participantApplicationInfos.isEmpty())
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

    public ResponseDto<?> finalSubmitInSecondPeriod(FinalSubmitRequestDto.SecondPeriod secondPeriodFinalSubmitRequestDto){

        Optional<User> findUser = userRepository.findById(secondPeriodFinalSubmitRequestDto.getAccountInfo().getUserId());
        if(findUser.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("대표자 정보를 조회할 수 없습니다.")
                    .build();
        }

        User user = findUser.get();
        Account refundAccount = accountRepository.save(Account.builder()
                        .accountBank(secondPeriodFinalSubmitRequestDto.getAccountInfo().getAccountBank())
                        .accountNumber(secondPeriodFinalSubmitRequestDto.getAccountInfo().getAccountNumber())
                        .depositOwnerName(secondPeriodFinalSubmitRequestDto.getAccountInfo().getDepositOwnerName())
                .build());
        secondPeriodRepository.save(SecondPeriod.builder()
                .user(user)
                .account(refundAccount)
//                        .isSecondApply()
                .build());

        secondPeriodFinalSubmitRequestDto.getParticipantApplicationInfos().getParticipantApplicationInfos().forEach(participantApplication ->
            universityApplicationRepository.save(
                    UniversityApplication.builder()
                            .user(user)
                            .eventName(participantApplication.getEventName())
                            .teamCount(participantApplication.getCancelParticipantCount())
                            .teamFee(participantApplication.getRefundParticipantFee())
                            .period(DatePeriod.SECOND.name())
                            .build()

            )
        );

        return ResponseDto.builder()
                .isSuccess(true)
                .message("2차 최종 제출이 성공적으로 완료되었습니다.")
                .build();
    }


    public ResponseDto<?> getFinalSubmitInfoInFirstPeriod(Long userId){

        List<ParticipantApplicationResponseDto.FirstPeriod> finalSubmitInfoInFirstPeriod = getParticipantApplicationInfoFromUniversityApplication(userId, DatePeriod.FIRST.name()).stream().map(ParticipantApplicationResponseDto.FirstPeriod::fromUniversityApplication).toList();

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(
                        FinalSubmitResponseDto.FirstPeriod.builder()
                                .participantApplicationInfos(new ParticipantApplicationsResponseDto<>(finalSubmitInfoInFirstPeriod))
                                .userInfo(getUserInfo(userId))
                                .accountInfo(getDepositAccountInfo(KUTCA_ID))
                                .build())
                .build();

    }

    public ResponseDto<?> getFinalSubmitInfoInSecondPeriod(Long userId){

        FinalSubmitResponseDto.Total finalSubmitInfoInSecondPeriod = getTotalParticipantApplicationInfoFromUniversityApplication(userId);

        if(finalSubmitInfoInSecondPeriod.isRefundExist()){
            Account refundAccount = secondPeriodRepository.findByUser_UserId(userId).get().getAccount();
            finalSubmitInfoInSecondPeriod.setRefundInfo(
                    AccountResponseDto.builder()
                            .accountBank(refundAccount.getAccountBank())
                            .accountNumber(refundAccount.getAccountNumber())
                            .depositOwnerName(refundAccount.getDepositOwnerName())
                            .build());
        }

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(finalSubmitInfoInSecondPeriod)
                .build();

    }


    private Map<String, Set<Integer>> initializeTeamCount(){
        Map<String, Set<Integer>> teamCount = new HashMap<>();
        teamCount.put("겨루기 단체전", new HashSet<>());
        teamCount.put("품새 단체전", new HashSet<>());
        teamCount.put("품새 페어", new HashSet<>());
        return teamCount;
    }

    private void countParticipantApplications(Long participantId, String gender, AtomicInteger individualCount, Map<String, Set<Integer>> teamCount, Long startEventId, Long endEventId, Long sparringEventId, Long poomsaeEventId){
        individualCount.addAndGet(participantApplicationRepository.countAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participantId, startEventId, endEventId));
        addParticipantApplicationsToTeamSet(participantId, teamCount.get("겨루기 단체전"), sparringEventId, false, gender);
        addParticipantApplicationsToTeamSet(participantId, teamCount.get("품새 단체전"), poomsaeEventId, false, gender);
    }

    private void countCancelParticipantApplications(Long participantId, AtomicInteger individualCount, Map<String, Set<Integer>> teamCount, Long startEventId, Long endEventId, Long sparringEventId, Long poomsaeEventId){
        individualCount.addAndGet(participantApplicationRepository.countAllByParticipant_ParticipantIdAndEvent_EventIdBetweenAndIs2ndCancelTrue(participantId, startEventId, endEventId));
//        addParticipantApplicationsToTeamSet(participantId, teamCount.get("겨루기 단체전"), sparringEventId, true);
//        addParticipantApplicationsToTeamSet(participantId, teamCount.get("품새 단체전"), poomsaeEventId, true);
    }

    private void addParticipantApplicationsToTeamSet(Long participantId, Set<Integer> teamSet, Long eventId, boolean isCancel, String gender){
        List<ParticipantApplication> findParticipantApplications;
        if(isCancel){
            findParticipantApplications = participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventIdAndIs2ndCancelTrue(participantId, eventId);
        }
        else {
            findParticipantApplications = participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(participantId, eventId);
        }
        //  겨루기 단체전의 경우 여자, 남자 겨루기 단체전의 eventTeamNumber 가져오는데 이는 다르게 받아들여야함 (둘 다 1인 경우 총 개수는 1개가 아닌 2개여야 함)
        findParticipantApplications.forEach(participantApplication -> teamSet.add(gender.equals("여성") ? participantApplication.getEventTeamNumber() : participantApplication.getEventTeamNumber() * 100));
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

    private List<ParticipantApplicationResponseDto.SecondPeriod> getParticipantApplicationInfosInSecondPeriod(List<UniversityApplicationResponseDto.FirstPeriod> universityApplicationInfos, AtomicInteger individualCount, Map<String, Set<Integer>> teamCount){

        int individualFee = eventRepository.findById(1L).get().getEventFee();
        int sparringTeamFee = eventRepository.findById(5L).get().getEventFee();
        int poomsaeTeamFee = eventRepository.findById(6L).get().getEventFee();
        int poomsaePairFee = eventRepository.findById(9L).get().getEventFee();

        return universityApplicationInfos.stream().map(universityApplicationInfo -> {

            ParticipantApplicationResponseDto.SecondPeriod participantApplicationInfo = ParticipantApplicationResponseDto.SecondPeriod.fromUniversityApplication(universityApplicationInfo);

            if("개인전".equals(universityApplicationInfo.getEventName())){
                participantApplicationInfo.setCancelParticipantCount(individualCount.get());
                participantApplicationInfo.setRefundParticipantFee(individualFee * individualCount.get());
            }

            if("겨루기 단체전".equals(universityApplicationInfo.getEventName())){
                int sparringTeamCount = teamCount.get(universityApplicationInfo.getEventName()).size();
                participantApplicationInfo.setCancelParticipantCount(sparringTeamCount);
                participantApplicationInfo.setRefundParticipantFee(sparringTeamCount * sparringTeamFee);
            }

            if("품새 단체전".equals(universityApplicationInfo.getEventName())){
                int poomsaeTeamCount = teamCount.get(universityApplicationInfo.getEventName()).size();
                participantApplicationInfo.setCancelParticipantCount(poomsaeTeamCount);
                participantApplicationInfo.setRefundParticipantFee(poomsaeTeamCount * poomsaeTeamFee);
            }

            if("품새 페어".equals(universityApplicationInfo.getEventName())){
                int poomsaePairCount = teamCount.get(universityApplicationInfo.getEventName()).size();
                participantApplicationInfo.setCancelParticipantCount(poomsaePairCount);
                participantApplicationInfo.setRefundParticipantFee(poomsaePairCount * poomsaePairFee);
            }

            return participantApplicationInfo;

        }).collect(Collectors.toList());

    }

    private List<UniversityApplicationResponseDto.FirstPeriod> getParticipantApplicationInfoFromUniversityApplication(Long userId, String period){

        List<UniversityApplication> findUniversityApplications = universityApplicationRepository.findAllByUser_UserIdAndPeriod(userId, period);
        return findUniversityApplications.stream().map(UniversityApplicationResponseDto.FirstPeriod::fromEntity).collect(Collectors.toList());

    }

    private FinalSubmitResponseDto.Total getTotalParticipantApplicationInfoFromUniversityApplication(Long userId){

        List<UniversityApplicationResponseDto.FirstPeriod> firstPeriodParticipantApplications = getParticipantApplicationInfoFromUniversityApplication(userId, DatePeriod.FIRST.name());
        List<ParticipantApplicationResponseDto.SecondPeriod> participantApplicationInfos = firstPeriodParticipantApplications.stream().map(universityApplication -> {
            ParticipantApplicationResponseDto.SecondPeriod participantApplicationInfo = ParticipantApplicationResponseDto.SecondPeriod.fromUniversityApplication(universityApplication);
            Optional<UniversityApplication> findSecondPeriodUniversityApplication = universityApplicationRepository.findByUser_UserIdAndEventNameAndPeriod(userId, universityApplication.getEventName(), DatePeriod.SECOND.name());
            if(findSecondPeriodUniversityApplication.isPresent()){
                UniversityApplication secondPeriodUniversityApplication = findSecondPeriodUniversityApplication.get();
                participantApplicationInfo.setCancelParticipantCount(secondPeriodUniversityApplication.getTeamCount());
                participantApplicationInfo.setRefundParticipantFee(secondPeriodUniversityApplication.getTeamFee());
            }
            return participantApplicationInfo;
        }).toList();

        return FinalSubmitResponseDto.Total.builder()
                .participantApplicationInfos(new ParticipantApplicationsResponseDto<>(participantApplicationInfos))
                .isRefundExist(!participantApplicationInfos.isEmpty())
                .build();

    }


}
