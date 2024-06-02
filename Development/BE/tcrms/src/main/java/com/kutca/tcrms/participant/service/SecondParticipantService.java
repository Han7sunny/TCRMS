package com.kutca.tcrms.participant.service;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.SecondParticipantRequestDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class SecondParticipantService {

    private final ParticipantRepository participantRepository;
    private final ParticipantApplicationRepository participantApplicationRepository;
    private final UserRepository userRepository;

    private static final Long SECOND_EVENT_ID = 10L;
    private final EventRepository eventRepository;

    @Transactional
    public ResponseDto<?> registSecondList(RequestDto<SecondParticipantRequestDto.Regist> secondParticipantRequestDto){

        Optional<User> findUser = userRepository.findById(secondParticipantRequestDto.getUserId());
        if(findUser.isEmpty()) {
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("대표자 정보를 찾을 수 없습니다.")
                    .build();
        }

        User user = findUser.get();
        Event event = eventRepository.findById(SECOND_EVENT_ID).get();
        AtomicInteger eventTeamNumber = new AtomicInteger(participantApplicationRepository.findTopByEvent_EventId(SECOND_EVENT_ID).map(pa -> pa.getEventTeamNumber() + 1).orElse(1));

        secondParticipantRequestDto.getRequestDtoList().forEach(second -> {

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
}
