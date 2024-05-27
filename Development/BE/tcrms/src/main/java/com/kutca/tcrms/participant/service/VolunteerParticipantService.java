package com.kutca.tcrms.participant.service;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.VolunteerParticipantRequestDto;
import com.kutca.tcrms.participant.controller.dto.response.VolunteerParticipantResponseDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Transactional
    public ResponseDto<?> registVolunteer(RequestDto<VolunteerParticipantRequestDto.Regist> volunteerParticipantRequestDto) {

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

            Optional<Participant> findParticipant = participantRepository.findByUser_UserIdAndNameAndPhoneNumber(user.getUserId(), volunteer.getName(), volunteer.getPhoneNumber());

            Participant participant = findParticipant.orElseGet(() -> participantRepository.save(
                    Participant.builder()
                            .user(user)
                            .name(volunteer.getName())
                            .gender(volunteer.getGender())
                            .universityName(user.getUniversityName())
                            .phoneNumber(volunteer.getPhoneNumber())
                            .build()));

            participantApplicationRepository.save(
                    ParticipantApplication.builder()
                            .participant(participant)
                            .event(VOLUNTEER_EVENT)
                            .eventTeamNumber(eventTeamNumber.getAndIncrement())
                            .build()
            );
        });

        return ResponseDto.builder()
                .isSuccess(true)
                .build();
    }

    @Transactional
    public ResponseDto<?> modifyVolunteer(VolunteerParticipantRequestDto.Modify volunteerParticipantRequestDto){

        Optional<Participant> findParticipant = participantRepository.findById(volunteerParticipantRequestDto.getParticipantId());
        if(findParticipant.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("참가자 정보를 찾을 수 없습니다.")
                    .build();
        }

        Participant participant = findParticipant.get().updateName(volunteerParticipantRequestDto.getName()).updatePhoneNumber(volunteerParticipantRequestDto.getPhoneNumber());

        if(!participant.getGender().equals(volunteerParticipantRequestDto.getGender())){
            participant.updateGender(volunteerParticipantRequestDto.getGender());

            List<ParticipantApplication> participantApplicationList = participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participant.getParticipantId(),1L, 4L);
            participantApplicationList.stream().forEach(pa -> {
                //  여 <-> 남
                if(participant.getGender().equals("여성"))
                    pa.updateEvent(eventRepository.findById(pa.getEvent().getEventId() + 2L).get());
                else
                    pa.updateEvent(eventRepository.findById(pa.getEvent().getEventId() - 2L).get());
                participantApplicationRepository.save(pa);
            });
        }

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(VolunteerParticipantResponseDto.fromEntity(participant))
                .build();
    }
}
