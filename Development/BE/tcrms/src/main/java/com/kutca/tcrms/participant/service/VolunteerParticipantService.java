package com.kutca.tcrms.participant.service;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.VolunteerParticipantRequestDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class VolunteerParticipantService {

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantApplicationRepository participantApplicationRepository;
    private final EventRepository eventRepository;
    private final Long VOLUNTEER_EVENT_ID = 11L;
    private final Event VOLUNTEER_EVENT = eventRepository.findById(VOLUNTEER_EVENT_ID).get();


    public ResponseDto<?> registVolunteer(RequestDto<VolunteerParticipantRequestDto.Regist> volunteerParticipantRequestDto) {

        //  자원봉사자의 정보가 기존에 있을 가능성 없다는 가정 하에 로직 작성

        Optional<User> findUser = userRepository.findById(volunteerParticipantRequestDto.getUserId());
        if(findUser.isEmpty()) {
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("대표자 정보를 찾을 수 없습니다.")
                    .build();
        }

        User user = findUser.get();
        AtomicInteger eventTeamNumber = new AtomicInteger(participantApplicationRepository.findTopByEvent_EventId(VOLUNTEER_EVENT_ID).map(pa -> pa.getEventTeamNumber() + 1).orElse(1));

        volunteerParticipantRequestDto.getRequestDtoList().stream().forEach(volunteer -> {
            Participant savedParticipant = participantRepository.save(Participant.builder()
                    .user(user)
                    .name(volunteer.getName())
                    .gender(volunteer.getGender())
                    .universityName(user.getUniversityName())
                    .phoneNumber(volunteer.getPhoneNumber())
                    .build());

            participantApplicationRepository.save(
                    ParticipantApplication.builder()
                            .participant(savedParticipant)
                            .event(VOLUNTEER_EVENT)
                            .eventTeamNumber(eventTeamNumber.getAndIncrement())
                            .build()
            );
        });

        return ResponseDto.builder()
                .isSuccess(true)
                .build();
    }
}
