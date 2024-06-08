package com.kutca.tcrms.participant;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.TeamMemberParticipantRequestDto;
import com.kutca.tcrms.participant.controller.dto.request.TeamParticipantRequestDto;
import com.kutca.tcrms.participant.controller.dto.response.TeamParticipantResponseDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participant.service.TeamParticipantService;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import com.kutca.tcrms.weightclass.entity.WeightClass;
import com.kutca.tcrms.weightclass.repository.WeightClassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TeamParticipantServiceTest {

    @InjectMocks
    private TeamParticipantService teamParticipantService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private ParticipantApplicationRepository participantApplicationRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private WeightClassRepository weightClassRepository;

    private User user;

    private Map<Long, WeightClass> weightClassMap = new HashMap<>();

    @BeforeEach
    void setUp(){
        user = User.builder()
                .userId(1L)
                .universityName("서울대학교")
                .build();

        weightClassMap.put(1L, WeightClass.builder().weightClassId(1L).build());
        weightClassMap.put(2L, WeightClass.builder().weightClassId(2L).build());
        weightClassMap.put(3L, WeightClass.builder().weightClassId(3L).build());
        weightClassMap.put(4L, WeightClass.builder().weightClassId(4L).build());
        weightClassMap.put(5L, WeightClass.builder().weightClassId(5L).build());

    }

    @Test
    @DisplayName("팀 신청 (기존 참가자 정보 존재) 성공")
    void registTeamListWithExistParticipantSuccess(){

        //  given

        Participant findParticipant1 = Participant.builder()
                .participantId(1L)
                .name("성춘향")
                .gender("여성")
                .identityNumber("001004-45671238")
                .isForeigner(false)
                .weightClass(weightClassMap.get(1L))
                .build();

        Participant findParticipant2 = Participant.builder()
                .participantId(2L)
                .name("Jennifer")
                .gender("여성")
                .isForeigner(true)
                .nationality("미국")
                .phoneNumber("kakao@gmail.com")
                .weightClass(weightClassMap.get(2L))
                .build();

        TeamParticipantRequestDto.Regist team1 = TeamParticipantRequestDto.Regist.builder()
                .eventId(5L)
                .teamMembers(Arrays.asList(
                        TeamMemberParticipantRequestDto.Regist.builder()
                                .name(findParticipant1.getName())
                                .identityNumber(findParticipant1.getIdentityNumber())
                                .gender(findParticipant1.getGender())
                                .isForeigner(findParticipant1.getIsForeigner())
                                .indexInTeam("1번 선수")
                                .weightClassId(1L)
                                .build(),
                        TeamMemberParticipantRequestDto.Regist.builder()
                                .name("Sara")
                                .identityNumber("990225-2012345")
                                .gender("여성")
                                .isForeigner(true)
                                .nationality("영국")
                                .indexInTeam("2번 선수")
                                .weightClassId(1L)
                                .build(),
                        TeamMemberParticipantRequestDto.Regist.builder()
                                .name(findParticipant2.getName())
                                .gender(findParticipant2.getGender())
                                .isForeigner(findParticipant2.getIsForeigner())
                                .nationality(findParticipant2.getNationality())
                                .phoneNumber(findParticipant2.getPhoneNumber())
                                .indexInTeam("3번 선수")
                                .weightClassId(2L)
                                .build(),
                        TeamMemberParticipantRequestDto.Regist.builder()
                                .name("김후보")
                                .identityNumber("030615-4561238")
                                .gender("여성")
                                .isForeigner(false)
                                .indexInTeam("후보 선수")
                                .weightClassId(2L)
                                .build()
                ))
                .build();

        TeamParticipantRequestDto.Regist team2 = TeamParticipantRequestDto.Regist.builder()
                .eventId(9L)
                .teamMembers(Arrays.asList(
                        TeamMemberParticipantRequestDto.Regist.builder()
                                .name(findParticipant1.getName())
                                .identityNumber(findParticipant1.getIdentityNumber())
                                .gender(findParticipant1.getGender())
                                .isForeigner(findParticipant1.getIsForeigner())
                                .indexInTeam("1번 선수")
                                .build(),
                        TeamMemberParticipantRequestDto.Regist.builder()
                                .name("이몽룡")
                                .identityNumber("991226-1234567")
                                .gender("남성")
                                .isForeigner(false)
                                .indexInTeam("2번 선수")
                                .build()
                ))
                .build();

        RequestDto<TeamParticipantRequestDto.Regist> registRequestDto = RequestDto.<TeamParticipantRequestDto.Regist>builder()
                .userId(user.getUserId())
                .requestDtoList(Arrays.asList(team1, team2))
                .build();

        Participant savedParticipant1 = Participant.builder()
                .participantId(3L)
                .name(team1.getTeamMembers().get(1).getName())
                .gender(team1.getTeamMembers().get(1).getGender())
                .isForeigner(team1.getTeamMembers().get(1).getIsForeigner())
                .nationality(team1.getTeamMembers().get(1).getNationality())
                .identityNumber(team1.getTeamMembers().get(1).getIdentityNumber())
                .phoneNumber(team1.getTeamMembers().get(1).getPhoneNumber())
                .weightClass(weightClassMap.get(team1.getTeamMembers().get(1).getWeightClassId()))
                .build();

        Participant savedParticipant2 = Participant.builder()
                .participantId(4L)
                .name(team1.getTeamMembers().get(3).getName())
                .gender(team1.getTeamMembers().get(3).getGender())
                .isForeigner(team1.getTeamMembers().get(3).getIsForeigner())
                .nationality(team1.getTeamMembers().get(3).getNationality())
                .identityNumber(team1.getTeamMembers().get(3).getIdentityNumber())
                .phoneNumber(team1.getTeamMembers().get(3).getPhoneNumber())
                .weightClass(weightClassMap.get(team1.getTeamMembers().get(3).getWeightClassId()))
                .build();

        Participant savedParticipant3 = Participant.builder()
                .participantId(5L)
                .name(team2.getTeamMembers().get(1).getName())
                .gender(team2.getTeamMembers().get(1).getGender())
                .isForeigner(team2.getTeamMembers().get(1).getIsForeigner())
                .nationality(team2.getTeamMembers().get(1).getNationality())
                .identityNumber(team2.getTeamMembers().get(1).getIdentityNumber())
                .phoneNumber(team2.getTeamMembers().get(1).getPhoneNumber())
                .weightClass(weightClassMap.get(team2.getTeamMembers().get(1).getWeightClassId()))
                .build();

        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(eventRepository.findById(5L)).willReturn(Optional.of(Event.builder().eventId(5L).eventName("단체전 여자 겨루기").build()));
        given(eventRepository.findById(9L)).willReturn(Optional.of(Event.builder().eventId(9L).eventName("단체전 혼성 품새").build()));
        given(participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), findParticipant1.getName(), findParticipant1.getIdentityNumber())).willReturn(Optional.of(findParticipant1));
//        given(participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), savedParticipant2.getName(), savedParticipant2.getIdentityNumber())).willReturn(Optional.of(savedParticipant2));
//        given(participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), savedParticipant1.getName(), savedParticipant1.getIdentityNumber())).willReturn(Optional.of(savedParticipant1));
//        given(participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), savedParticipant3.getName(), savedParticipant3.getIdentityNumber())).willReturn(Optional.of(savedParticipant3));
        given(participantRepository.findByUser_UserIdAndNameAndPhoneNumber(user.getUserId(), findParticipant2.getName(), findParticipant2.getPhoneNumber())).willReturn(Optional.of(findParticipant2));
//        given(participantRepository.save(any(Participant.class))).willReturn(savedParticipant1);
//        given(participantRepository.save(any(Participant.class))).willReturn(savedParticipant2);
//        given(participantRepository.save(any(Participant.class))).willReturn(savedParticipant3);
        given(weightClassRepository.findById(1L)).willReturn(Optional.of(weightClassMap.get(1L)));
        given(weightClassRepository.findById(2L)).willReturn(Optional.of(weightClassMap.get(2L)));



        //  when
        ResponseDto<?> responseDto = teamParticipantService.registTeamList(registRequestDto);

        //  then
        assertTrue(responseDto.getIsSuccess());

        verify(participantRepository, times(2)).findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), findParticipant1.getName(), findParticipant1.getIdentityNumber());
        verify(participantRepository, times(0)).findByUser_UserIdAndNameAndPhoneNumber(user.getUserId(), findParticipant1.getName(), findParticipant1.getIdentityNumber());
        verify(participantRepository, times(3)).save(any(Participant.class));

    }
}
