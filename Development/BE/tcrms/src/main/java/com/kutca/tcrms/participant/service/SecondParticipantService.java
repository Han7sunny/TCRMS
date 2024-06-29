package com.kutca.tcrms.participant.service;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.SecondParticipantRequestDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantsResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.SecondParticipantResponseDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecondParticipantService {

    private final ParticipantRepository participantRepository;
    private final ParticipantApplicationRepository participantApplicationRepository;
    private final UserRepository userRepository;

    private static final Long SECOND_EVENT_ID = 10L;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public ResponseDto<?> getSecondList(Long userId){

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
                    .message("세컨 신청 내역이 없습니다.")
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
                .payload(ParticipantResponseDto.builder()
                        .isParticipantExists(true)
                        .isEditable(user.getIsEditable())
                        .isDepositConfirmed(user.getIsDepositConfirmed())
                        .participants(findParticipantList.stream()
                                .map(participant -> {
                                    Optional<ParticipantApplication> findParticipantApplication = participantApplicationRepository.findByParticipant_ParticipantIdAndEvent_EventId(participant.getParticipantId(), SECOND_EVENT_ID);
                                    return findParticipantApplication.map(participantApplication -> SecondParticipantResponseDto.fromEntity(participant, participantApplication.getParticipantApplicationId())).orElse(null);
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList()))
                        .build())
                .build();
    }

    @Transactional
    public ResponseDto<?> registSecondList(RequestDto<SecondParticipantRequestDto.Regist> secondParticipantRequestDto) {

        Optional<User> findUser = userRepository.findById(secondParticipantRequestDto.getUserId());
        if (findUser.isEmpty()) {
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("대표자 정보를 찾을 수 없습니다.")
                    .build();
        }

        User user = findUser.get();
        Event event = eventRepository.findById(SECOND_EVENT_ID).get();
        AtomicInteger eventTeamNumber = new AtomicInteger(participantApplicationRepository.findTopByEvent_EventId(SECOND_EVENT_ID).map(pa -> pa.getEventTeamNumber() + 1).orElse(1));

        secondParticipantRequestDto.getParticipants().forEach(second -> {

            Optional<Participant> findParticipant = (second.getIsForeigner() && second.getIdentityNumber() == null)
                    ? participantRepository.findByUser_UserIdAndNameAndPhoneNumber(user.getUserId(), second.getName(), second.getPhoneNumber())
                    : participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), second.getName(), second.getIdentityNumber());

            Participant participant = findParticipant.orElseGet(() -> participantRepository.save(
                    Participant.builder()
                            .user(user)
                            .name(second.getName())
                            .gender(second.getGender())
                            .isForeigner(second.getIsForeigner())
                            .nationality(second.getNationality())
                            .universityName(user.getUniversityName())
                            .phoneNumber(second.getPhoneNumber())
                            .build()));

            participantApplicationRepository.save(
                    ParticipantApplication.builder()
                            .participant(participant)
                            .event(event)
                            .eventTeamNumber(eventTeamNumber.getAndIncrement())
                            .build()
            );

        });

        return ResponseDto.builder()
                .isSuccess(true)
                .build();
    }

    @Transactional
    public ResponseDto<?> modifySecond(SecondParticipantRequestDto.Modify secondParticipantRequestDto) {

        Optional<Participant> findParticipant = participantRepository.findById(secondParticipantRequestDto.getParticipantId());
        if (findParticipant.isEmpty()) {
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("참가자 정보를 찾을 수 없습니다.")
                    .build();
        }

        Participant participant = findParticipant.get();

        if(!participant.getGender().equals(secondParticipantRequestDto.getGender())){

            List<ParticipantApplication> participantApplicationList = participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participant.getParticipantId(),1L, 4L);
            participantApplicationList.forEach(pa -> {
                //  여 <-> 남
                if(participant.getGender().equals("여성"))
                    pa.updateEvent(eventRepository.findById(pa.getEvent().getEventId() + 2L).get());
                else
                    pa.updateEvent(eventRepository.findById(pa.getEvent().getEventId() - 2L).get());
                participantApplicationRepository.save(pa);
            });
        }

        Participant modifiedSecondParticipant = participantRepository.save(participant.updateSecond(secondParticipantRequestDto));

        return ResponseDto.builder()
                .isSuccess(true)
                .message("세컨 정보가 성공적으로 수정되었습니다.")
                .payload(SecondParticipantResponseDto.fromEntity(modifiedSecondParticipant, secondParticipantRequestDto.getParticipantApplicationId()))
                .build();
    }

    @Transactional
    public ResponseDto<?> deleteSecond(SecondParticipantRequestDto.Delete secondParticipantRequestDto){
        participantApplicationRepository.deleteById(secondParticipantRequestDto.getParticipantApplicationId());

        //  학교별 신청 종목 팀 데이터 변경

        Boolean isOtherParticipantApplicationExist = participantApplicationRepository.existsByParticipant_ParticipantId(secondParticipantRequestDto.getParticipantId());

        if(!isOtherParticipantApplicationExist){
            participantRepository.deleteById(secondParticipantRequestDto.getParticipantId());
            //  participant_file
            //  file
        }

        return ResponseDto.builder()
                .isSuccess(true)
                .build();
    }
}