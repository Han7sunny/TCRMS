package com.kutca.tcrms.participant.service;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.TeamParticipantRequestDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import com.kutca.tcrms.weightclass.repository.WeightClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamParticipantService {

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantApplicationRepository participantApplicationRepository;
    private final EventRepository eventRepository;
    private final WeightClassRepository weightClassRepository;

    @Transactional
    public ResponseDto<?> registTeamList(RequestDto<TeamParticipantRequestDto.Regist> teamParticipantRequestDto){

        Optional<User> findUser = userRepository.findById(teamParticipantRequestDto.getUserId());
        if(findUser.isEmpty()) {
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("대표자 정보를 찾을 수 없습니다.")
                    .build();
        }

        User user = findUser.get();
        teamParticipantRequestDto.getRequestDtoList().forEach(team -> {
            Event event = eventRepository.findById(team.getEventId()).get();
            int eventTeamNumber = participantApplicationRepository.findTopByEvent_EventId(event.getEventId()).map(pa -> pa.getEventTeamNumber() + 1).orElse(1);
            team.getTeamMembers().forEach(teamMember -> {

                Optional<Participant> findParticipant = (teamMember.getIsForeigner() && teamMember.getIdentityName() == null)
                        ? participantRepository.findByUser_UserIdAndNameAndPhoneNumber(user.getUserId(), teamMember.getName(), teamMember.getPhoneNumber())
                        : participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), teamMember.getName(), teamMember.getIdentityName());

                Participant participant = findParticipant.orElseGet(() -> participantRepository.save(
                        Participant.builder()
                                .user(user)
                                .name(teamMember.getName())
                                .gender(teamMember.getGender())
                                .isForeigner(teamMember.getIsForeigner())
                                .nationality(teamMember.getNationality())
                                .universityName(user.getUniversityName())
                                .phoneNumber(teamMember.getPhoneNumber())
                                .weightClass(teamMember.getWeightClassId() == null ? null : weightClassRepository.findById(teamMember.getWeightClassId()).get())
                                .build()
                ));

                participantApplicationRepository.save(
                        ParticipantApplication.builder()
                                .participant(participant)
                                .event(event)
                                .eventTeamNumber(eventTeamNumber)
                                .indexInTeam(teamMember.getIndexInTeam())
                                .build()
                );
            });
        });

        return ResponseDto.builder()
                .isSuccess(true)
                .build();
    }
}
