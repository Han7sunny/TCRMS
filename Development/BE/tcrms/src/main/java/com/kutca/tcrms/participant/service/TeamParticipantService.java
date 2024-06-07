package com.kutca.tcrms.participant.service;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.TeamParticipantRequestDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantsResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.TeamMemberParticipantResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.TeamParticipantResponseDto;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamParticipantService {

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantApplicationRepository participantApplicationRepository;
    private final EventRepository eventRepository;
    private final WeightClassRepository weightClassRepository;

    @Transactional(readOnly = true)
    public ResponseDto<?> getTeamList(Long userId){

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
                    .message("단체전 신청 내역이 없습니다.")
                    .payload(
                            ParticipantResponseDto.builder()
                                    .isEditable(user.getIsEditable())
                                    .isDepositConfirmed(user.getIsDepositConfirmed())
                                    .isParticipantExists(false)
                                    .build()
                    )
                    .build();
        }

        Map<Integer, List<ParticipantApplication>> participantApplicationByEventTeamNumber = new HashMap<>();

        findParticipantList.forEach(participant -> {
            List<ParticipantApplication> findParticipantApplicationList = participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participant.getParticipantId(), 5L, 9L);
            findParticipantApplicationList.forEach(pa -> participantApplicationByEventTeamNumber.computeIfAbsent(pa.getEventTeamNumber(), k -> new ArrayList<>()).add(pa));
        });

        List<TeamParticipantResponseDto> teams = new ArrayList<>();

        participantApplicationByEventTeamNumber.forEach((eventTeamNumber, participantApplicationList) -> {
            teams.add(TeamParticipantResponseDto.builder()
                    .eventTeamNumber(eventTeamNumber)
                    .eventId(participantApplicationList.get(0).getEvent().getEventId())
                    .teamMembers(participantApplicationList.stream().map(pa -> {
                        WeightClass weightClass = pa.getParticipant().getWeightClass();
                        return TeamMemberParticipantResponseDto.builder()
                                .participantApplicationId(pa.getParticipantApplicationId())
                                .participantId(pa.getParticipant().getParticipantId())
                                .weightClassId(weightClass == null ? null : weightClass.getWeightClassId())
                                .name(pa.getParticipant().getName())
                                .identityNumber(pa.getParticipant().getIdentityNumber())
                                .gender(pa.getParticipant().getGender())
                                .isForeigner(pa.getParticipant().getIsForeigner())
                                .nationality(pa.getParticipant().getNationality())
                                .phoneNumber(pa.getParticipant().getPhoneNumber())
                                .indexInTeam(pa.getIndexInTeam())
                                .build();
                    })
                            .collect(Collectors.toList()))
                    .build()
            );

        });

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(
                        ParticipantResponseDto.builder()
                                .isEditable(user.getIsEditable())
                                .isDepositConfirmed(user.getIsDepositConfirmed())
                                .isParticipantExists(true)
                                .participants(new ParticipantsResponseDto<>(new ArrayList<>(teams)))
                                .build()
                )
                .build();

    }

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

                Optional<Participant> findParticipant = (teamMember.getIsForeigner() && teamMember.getIdentityNumber() == null)
                        ? participantRepository.findByUser_UserIdAndNameAndPhoneNumber(user.getUserId(), teamMember.getName(), teamMember.getPhoneNumber())
                        : participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), teamMember.getName(), teamMember.getIdentityNumber());

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

    @Transactional
    public ResponseDto<?> modifyTeam(TeamParticipantRequestDto.Modify teamParticipantRequestDto){

        User user = userRepository.findById(teamParticipantRequestDto.getUserId()).get();
        Event event = eventRepository.findById(teamParticipantRequestDto.getEventId()).get();
        int eventTeamNumber = teamParticipantRequestDto.getEventTeamNumber();

        List<TeamMemberParticipantResponseDto> teamMembers = teamParticipantRequestDto.getTeamMembers().stream().map(teamMember -> {

            Long participantApplicationId = teamMember.getParticipantApplicationId();
            WeightClass weightClass = weightClassRepository.findById(teamMember.getWeightClassId()).get();

            Optional<Participant> findParticipant = (teamMember.getIsForeigner() && teamMember.getIdentityNumber() == null)
                    ? participantRepository.findByUser_UserIdAndNameAndPhoneNumber(user.getUserId(), teamMember.getName(), teamMember.getPhoneNumber())
                    : participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), teamMember.getName(), teamMember.getIdentityNumber());

            Participant participant = findParticipant.orElseGet(() -> participantRepository.save(
                    Participant.builder()
                            .name(teamMember.getName())
                            .identityNumber(teamMember.getIdentityNumber())
                            .gender(teamMember.getGender())
                            .universityName(user.getUniversityName())
                            .isForeigner(teamMember.getIsForeigner())
                            .nationality(teamMember.getNationality())
                            .user(user)
                            .weightClass(weightClass)
                            .build()
            ));

            if(findParticipant.isPresent()){
                if(teamMember.getIsWeightClassChange()) {
                    participant.updateWeightClass(weightClass);
                }

                if(teamMember.getIsParticipantChange()){
                    participant.updateTeamMember(teamMember);
                }
            }

            if(!participantApplicationRepository.existsByEventTeamNumberAndIndexInTeam(eventTeamNumber, teamMember.getIndexInTeam())){
                participantApplicationId = participantApplicationRepository.save(
                        ParticipantApplication.builder()
                                .participant(participant)
                                .event(event)
                                .eventTeamNumber(eventTeamNumber)
                                .is2ndCancel(false)
                                .is2ndChange(true)
                                .indexInTeam(teamMember.getIndexInTeam())
                        .build()).getParticipantApplicationId();
            }

            return TeamMemberParticipantResponseDto.builder()
                    .participantId(participant.getParticipantId())
                    .participantApplicationId(participantApplicationId)
                    .weightClassId(weightClass.getWeightClassId())
                    .name(participant.getName())
                    .identityNumber(participant.getIdentityNumber())
                    .gender(participant.getGender())
                    .isForeigner(participant.getIsForeigner())
                    .nationality(participant.getNationality())
                    .phoneNumber(participant.getPhoneNumber())
                    .indexInTeam(teamMember.getIndexInTeam())
                    .build();

        }).collect(Collectors.toList());

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(
                        TeamParticipantResponseDto.builder()
                                .eventTeamNumber(eventTeamNumber)
                                .eventId(event.getEventId())
                                .teamMembers(teamMembers)
                                .build()
                )
                .build();
    }

    @Transactional
    public ResponseDto<?> deleteTeam(TeamParticipantRequestDto.Delete teamParticipantRequestDto){

        Long eventId = teamParticipantRequestDto.getEventId();

        teamParticipantRequestDto.getParticipantApplicationIds().forEach(participantApplicationRepository::deleteById);

        teamParticipantRequestDto.getParticipantIds().forEach(participantId -> {
            if(!participantApplicationRepository.existsByParticipant_ParticipantIdAndEvent_EventId(participantId, eventId)){
                participantRepository.deleteById(participantId);
                //  participant_file
                //  file
            }
        });

        //  학교별 신청 종목 팀 데이터 변경

        return ResponseDto.builder()
                .isSuccess(true)
                .build();
    }
}
