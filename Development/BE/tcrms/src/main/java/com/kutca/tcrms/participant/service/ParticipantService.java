package com.kutca.tcrms.participant.service;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.IndividualParticipantRequestDto;
import com.kutca.tcrms.participant.controller.dto.response.IndividualParticipantResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantsResponseDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import com.kutca.tcrms.weightclass.entity.WeightClass;
import com.kutca.tcrms.weightclass.repository.WeightClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final WeightClassRepository weightClassRepository;
    private final ParticipantApplicationRepository participantApplicationRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public ResponseDto<?> getIndividualList(Long userId) {

        Optional<User> findUser = userRepository.findById(userId);
        if(findUser.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("대표자 정보를 찾을 수 없습니다.")
                    .build();
        }

        User user = findUser.get();
        List<Participant> findParticipantList = participantRepository.findAllByUser_UserId(userId);
        if(findParticipantList.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(true)
                    .payload(
                            ParticipantResponseDto.builder()
                                    .isEditable(user.getIsEditable())
                                    .isDepositConfirmed(user.getIsDepositConfirmed())
                                    .isParticipantExists(false)
                                    .build()

                    )
                    .build();
        }

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(
                        ParticipantResponseDto.builder()
                                .isEditable(user.getIsEditable())
                                .isDepositConfirmed(user.getIsDepositConfirmed())
                                .isParticipantExists(true)
                                .participants(new ParticipantsResponseDto<>(findParticipantList.stream().map(
                                        participant -> IndividualParticipantResponseDto.fromEntity(participant, participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participant.getParticipantId(), 1L, 4L))
                                ).collect(Collectors.toList())))
                                .build()
                )
                .build();
    }

    @Transactional
    public ResponseDto<?> registIndividualList(RequestDto<IndividualParticipantRequestDto.Regist> individualParticipantRequestDto) {

        Optional<User> findUser = userRepository.findById(individualParticipantRequestDto.getUserId());
        if(findUser.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("대표자 정보를 찾을 수 없습니다.")
                    .build();
        }

        User user = findUser.get();

        List<Event> eventList = eventRepository.findAllByEventIdBetween(1L, 4L);

        individualParticipantRequestDto.getRequestDtoList().stream().forEach(participant -> {

            Optional<Participant> findParticipant = (participant.getIsForeigner() && participant.getIdentityNumber() == null)
                    ? participantRepository.findByUser_UserIdAndNameAndPhoneNumber(user.getUserId(), participant.getName(), participant.getPhoneNumber())
                    : participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), participant.getName(), participant.getIdentityNumber());

            Participant individualParticipant = findParticipant.isEmpty()
                    ? participantRepository.save(
                        Participant.builder()
                                .name(participant.getName())
                                .identityNumber(participant.getIdentityNumber())
                                .gender(participant.getGender())
                                .universityName(user.getUniversityName())
                                .isForeigner(participant.getIsForeigner())
                                .nationality(participant.getNationality())
                                .user(user)
                                .weightClass(participant.getWeightClassId() == null ? null : weightClassRepository.findById(participant.getWeightClassId()).get())
                                .build())
                    : findParticipant.get();

            participant.getEventIds().stream().forEach(eventId -> {
                    Optional<ParticipantApplication> findParticipantApplication = participantApplicationRepository.findTopByEvent_EventId(eventId);
                    int eventTeamNumber = findParticipantApplication.isEmpty() ? 1 : findParticipantApplication.get().getEventTeamNumber() + 1;
                    participantApplicationRepository.save(
                        ParticipantApplication.builder()
                            .participant(individualParticipant)
                            .event(eventList.get(Math.toIntExact(eventId)))
                            .eventTeamNumber(eventTeamNumber)
                            .is2ndCancel(null)
                            .is2ndChange(null)
                            .build()
                    );

            });

        });

        return ResponseDto.builder()
                .isSuccess(true)
                .build();
    }

    @Transactional
    public ResponseDto<?> modifyIndividual(IndividualParticipantRequestDto.Modify individualParticipantRequestDto) {
        //  2차 기간에 종목 변경 불가능 (event) -> 종목 disabled
            //  값 넘어올 수도 있고 (1차), 안 넘어올 수도 있음(2차)
            //  값 넘어올 경우 ParticipantApplicationId : eventId

        //  체급은 둘 다 변경 가능 (weightclass)

        Optional<Participant> findParticipant = participantRepository.findById(individualParticipantRequestDto.getParticipantId());
        if(findParticipant.isEmpty()) {
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("참가자 정보를 찾을 수 없습니다.")
                    .build();
        }

        Participant participant = findParticipant.get();

        if(individualParticipantRequestDto.getIsParticipantChange()) {
            participant.updateParticipant(individualParticipantRequestDto);
        }

        List<ParticipantApplication> participantApplicationList = new ArrayList<>();
        if(individualParticipantRequestDto.getIsEventChange()) {

            Map<Long, Long> participantApplicationInfos = individualParticipantRequestDto.getEventInfo();

            for (Map.Entry<Long, Long> participantApplicationInfo: participantApplicationInfos.entrySet()) {

                Long eventId = participantApplicationInfo.getKey();
                Long participantApplicationId = participantApplicationInfo.getValue();

                Optional<Event> findEvent = eventRepository.findById(eventId);
                if(findEvent.isEmpty()) {
                    return ResponseDto.builder()
                            .isSuccess(false)
                            .message("종목 정보를 찾을 수 없습니다.")
                            .build();
                }

                Event event = findEvent.get();
                int eventTeamNumber = participantApplicationRepository.findTopByEvent_EventId(eventId).map(pa -> pa.getEventTeamNumber() + 1).orElse(1);
                
                //  학교별 신청 종목 팀의 팀개수 갱신 로직 추가
                
                ParticipantApplication participantApplication = null;

                if(participantApplicationId == null) {
                    participantApplication = ParticipantApplication.builder()
                            .participant(participant)
                            .event(event)
                            .eventTeamNumber(eventTeamNumber)
                            .build();
                }
                else{
                    participantApplication = participantApplicationRepository.findById(participantApplicationId).get().updateEvent(event).updateEventTeamNumber(eventTeamNumber);
                }

                participantApplicationList.add(participantApplicationRepository.save(participantApplication));
            }
        }

        if(individualParticipantRequestDto.getIsWeightClassChange()) {
            Optional<WeightClass> findWeightClass = weightClassRepository.findById(individualParticipantRequestDto.getWeightClassId());
            if(findWeightClass.isEmpty()) {
                return ResponseDto.builder()
                        .isSuccess(false)
                        .message("체급 정보를 찾을 수 없습니다.")
                        .build();
            }

            WeightClass weightClass = findWeightClass.get();
            participant.updateWeightClass(weightClass);
        }

        Participant modifiedParticipant = participantRepository.save(participant);

        return ResponseDto.builder()
                .isSuccess(true)
                .message("참가자 정보가 성공적으로 수정되었습니다.")
                .payload(IndividualParticipantResponseDto.fromEntity(modifiedParticipant, participantApplicationList))
                .build();
    }

}
