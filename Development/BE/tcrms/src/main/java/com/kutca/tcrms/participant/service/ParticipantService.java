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

import java.util.List;
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
                                .participants(new ParticipantsResponseDto<>(findParticipantList.stream().map(participant -> {
                                    List<ParticipantApplication> findParticipantApplicationList = participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participant.getParticipantId(), 1L, 4L);
                                    List<Event> eventList = findParticipantApplicationList.stream().map(ParticipantApplication::getEvent).toList();
                                    return IndividualParticipantResponseDto.fromEntity(participant, participant.getWeightClass().getWeightClassId(), eventList);
                                        }).collect(Collectors.toList())))
                                .build()
                )
                .build();
    }

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

}
