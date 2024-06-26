package com.kutca.tcrms.participant;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.VolunteerParticipantRequestDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.VolunteerParticipantResponseDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participant.service.VolunteerParticipantService;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VolunteerParticipantServiceTest {

    @InjectMocks
    private VolunteerParticipantService volunteerParticipantService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private ParticipantApplicationRepository participantApplicationRepository;

    @Mock
    private EventRepository eventRepository;

    private final Long VOLUNTEER_EVENT_ID = 11L;

    private User user;

    private Event event;

    private RequestDto<VolunteerParticipantRequestDto.Regist> registRequestDto;

    @BeforeEach
    public void setUp(){
        user = User.builder()
                .userId(1L)
                .universityName("서울대학교")
                .build();

        event = Event.builder()
                .eventId(VOLUNTEER_EVENT_ID)
                .eventName("자원봉사자")
                .build();

        VolunteerParticipantRequestDto.Regist volunteer1 = VolunteerParticipantRequestDto.Regist.builder()
                .name("홍길동")
                .gender("남자")
                .phoneNumber("010-1234-5678")
                .build();

        VolunteerParticipantRequestDto.Regist volunteer2 = VolunteerParticipantRequestDto.Regist.builder()
                .name("Jennifer")
                .gender("여자")
                .phoneNumber("love119@gmail.com")
                .build();

        VolunteerParticipantRequestDto.Regist volunteer3 = VolunteerParticipantRequestDto.Regist.builder()
                .name("성춘향")
                .gender("여자")
                .phoneNumber("010-2345-9876")
                .build();

        registRequestDto = RequestDto.<VolunteerParticipantRequestDto.Regist>builder()
                .userId(user.getUserId())
                .participants(Arrays.asList(volunteer1, volunteer2, volunteer3))
                .build();
    }


    @Test
    @DisplayName("자원봉사자 신청 성공")
    public void registVolunteerWithNewParticipantSuccess(){

        //  given
        Participant savedVolunteer1 = Participant.builder()
                .participantId(1L)
                .name(registRequestDto.getParticipants().get(0).getName())
                .gender(registRequestDto.getParticipants().get(0).getGender())
                .universityName(user.getUniversityName())
                .phoneNumber(registRequestDto.getParticipants().get(0).getPhoneNumber())
                .build();

        Participant savedVolunteer2 = Participant.builder()
                .participantId(2L)
                .name(registRequestDto.getParticipants().get(1).getName())
                .gender(registRequestDto.getParticipants().get(1).getGender())
                .universityName(user.getUniversityName())
                .phoneNumber(registRequestDto.getParticipants().get(1).getPhoneNumber())
                .build();

        Participant savedVolunteer3 = Participant.builder()
                .participantId(3L)
                .name(registRequestDto.getParticipants().get(2).getName())
                .gender(registRequestDto.getParticipants().get(2).getGender())
                .universityName(user.getUniversityName())
                .phoneNumber(registRequestDto.getParticipants().get(2).getPhoneNumber())
                .build();

        ParticipantApplication savedVolunteerApplication1 = ParticipantApplication.builder()
                .participantApplicationId(1L)
                .participant(savedVolunteer1)
                .event(event)
                .eventTeamNumber(1)
                .build();

        ParticipantApplication savedVolunteerApplication2 = ParticipantApplication.builder()
                .participantApplicationId(2L)
                .participant(savedVolunteer2)
                .event(event)
                .eventTeamNumber(2)
                .build();

        ParticipantApplication savedVolunteerApplication3 = ParticipantApplication.builder()
                .participantApplicationId(3L)
                .participant(savedVolunteer3)
                .event(event)
                .eventTeamNumber(3)
                .build();

        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(eventRepository.findById(VOLUNTEER_EVENT_ID)).willReturn(Optional.of(event));
        given(participantApplicationRepository.findTopByEvent_EventId(VOLUNTEER_EVENT_ID)).willReturn(Optional.of(savedVolunteerApplication3));
        given(participantRepository.findAllByUser_UserId(user.getUserId())).willReturn(Arrays.asList(savedVolunteer1, savedVolunteer2, savedVolunteer3));

        //  when
        ResponseDto<?> responseDto = volunteerParticipantService.registVolunteer(registRequestDto);

        //  then
        verify(userRepository).findById(user.getUserId());
        verify(eventRepository).findById(VOLUNTEER_EVENT_ID);
        verify(participantApplicationRepository, times(1)).findTopByEvent_EventId(VOLUNTEER_EVENT_ID);

        assertTrue(responseDto.getIsSuccess());
        List<Participant> findParticipantList = participantRepository.findAllByUser_UserId(user.getUserId());
        assertEquals(findParticipantList.size(), registRequestDto.getParticipants().size());
        assertTrue(findParticipantList.stream().allMatch(participant -> user.getUniversityName().equals(participant.getUniversityName())));
        assertEquals(participantApplicationRepository.findTopByEvent_EventId(VOLUNTEER_EVENT_ID).get().getEventTeamNumber(), registRequestDto.getParticipants().size());
    }

    @Test
    @DisplayName("자원봉사자 성별 남->여 수정 성공")
    public void modifyVolunteerSuccess(){

        //  given
        VolunteerParticipantRequestDto.Modify modifyRequestDto = VolunteerParticipantRequestDto.Modify.builder()
                .participantId(1L)
                .name("홍길동")
                .gender("여자")
                .phoneNumber("010-1234-5678")
                .build();

        Participant savedVolunteer = Participant.builder()
                .participantId(modifyRequestDto.getParticipantId())
                .name(modifyRequestDto.getName())
                .identityNumber("980316-1234567")
                .gender("남자")
                .universityName(user.getUniversityName())
                .phoneNumber(modifyRequestDto.getPhoneNumber())
                .build();

        Event event1 = Event.builder().eventId(1L).eventName("여자 개인 겨루기").build();
        Event event2 = Event.builder().eventId(2L).eventName("여자 개인 품새").build();
        Event event3 = Event.builder().eventId(3L).eventName("남자 개인 겨루기").build();
        Event event4 = Event.builder().eventId(4L).eventName("남자 개인 품새").build();

        ParticipantApplication savedParticipantApplication1 = ParticipantApplication.builder()
                .participantApplicationId(1L)
                .participant(savedVolunteer)
                .event(event3)
                .eventTeamNumber(1)
                .build();

        ParticipantApplication savedParticipantApplication2 = ParticipantApplication.builder()
                .participantApplicationId(2L)
                .participant(savedVolunteer)
                .event(event4)
                .eventTeamNumber(1)
                .build();

        ParticipantApplication savedParticipantApplication3 = ParticipantApplication.builder()
                .participantApplicationId(3L)
                .participant(savedVolunteer)
                .event(event)
                .eventTeamNumber(1)
                .build();

        given(participantRepository.findById(modifyRequestDto.getParticipantId())).willReturn(Optional.of(savedVolunteer));
        given(participantRepository.save(any(Participant.class))).willReturn(savedVolunteer);
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(modifyRequestDto.getParticipantId(), 1L, 4L))
                .willReturn(Arrays.asList(savedParticipantApplication1, savedParticipantApplication2));
        given(eventRepository.findById(event1.getEventId())).willReturn(Optional.of(event1));
        given(eventRepository.findById(event2.getEventId())).willReturn(Optional.of(event2));

        //  when
        ResponseDto<?> responseDto = volunteerParticipantService.modifyVolunteer(modifyRequestDto);

        //  then
        assertTrue(responseDto.getIsSuccess());
        assertEquals(((VolunteerParticipantResponseDto) responseDto.getPayload()).getGender(), modifyRequestDto.getGender());

        verify(eventRepository, times(2)).findById(anyLong());
    }

    @Test
    @DisplayName("자원봉사자 신청 확인(조회) 성공")
    public void getVolunteerList(){

        //  given
        Participant findParticipant1 = Participant.builder()
                .participantId(1L)
                .name("홍길동")
                .gender("남성")
                .identityNumber("981004-1234567")
                .universityName(user.getUniversityName())
                .build();

        Participant findParticipant2 = Participant.builder()
                .participantId(2L)
                .name(findParticipant1.getName())
                .gender(findParticipant1.getName())
                .universityName(user.getUniversityName())
                .phoneNumber("010-1234-5678")
                .build();

        Participant findParticipant3 = Participant.builder()
                .participantId(3L)
                .name("성춘향")
                .gender("여성")
                .universityName(user.getUniversityName())
                .phoneNumber("010-2345-6789")
                .build();

        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(participantRepository.findAllByUser_UserId(user.getUserId())).willReturn(Arrays.asList(findParticipant1, findParticipant2, findParticipant3));
        given(participantApplicationRepository.existsByParticipant_ParticipantIdAndEvent_EventId(findParticipant1.getParticipantId(), VOLUNTEER_EVENT_ID)).willReturn(false);
        given(participantApplicationRepository.existsByParticipant_ParticipantIdAndEvent_EventId(findParticipant2.getParticipantId(), VOLUNTEER_EVENT_ID)).willReturn(true);
        given(participantApplicationRepository.existsByParticipant_ParticipantIdAndEvent_EventId(findParticipant3.getParticipantId(), VOLUNTEER_EVENT_ID)).willReturn(true);

        //  when
        ResponseDto<?> responseDto = volunteerParticipantService.getVolunteerList(user.getUserId());

        //  then
        assertTrue(responseDto.getIsSuccess());
        List<VolunteerParticipantResponseDto> volunteerResponseDtoList = ((ParticipantResponseDto)responseDto.getPayload()).getParticipants();
        assertEquals(volunteerResponseDtoList.size(), 2);

        verify(participantApplicationRepository, times(3)).existsByParticipant_ParticipantIdAndEvent_EventId(anyLong(), anyLong());
    }
}
