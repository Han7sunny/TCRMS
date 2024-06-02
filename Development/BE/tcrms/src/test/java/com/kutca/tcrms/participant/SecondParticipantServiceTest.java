package com.kutca.tcrms.participant;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.SecondParticipantResponseDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participant.service.SecondParticipantService;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class SecondParticipantServiceTest {

    @InjectMocks
    private SecondParticipantService secondParticipantService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private ParticipantApplicationRepository participantApplicationRepository;

    @Mock
    private EventRepository eventRepository;

    private User user;

    private Event event;

    private final Long SECOND_EVENT_ID = 10L;

    @BeforeEach
    public void setUp(){
        user = User.builder()
                .userId(1L)
                .universityName("서울대학교")
                .build();

        event = Event.builder()
                .eventId(SECOND_EVENT_ID)
                .eventName("세컨")
                .build();
    }

    @Test
    @DisplayName("세컨 신청 확인(조회) 성공")
    void getSecondListWithParticipants(){

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
                .name("성춘향")
                .gender("여성")
                .universityName(user.getUniversityName())
                .phoneNumber("010-2345-6789")
                .build();

        Participant findParticipant3 = Participant.builder()
                .participantId(3L)
                .name("이몽룡")
                .gender("남성")
                .universityName(user.getUniversityName())
                .phoneNumber("010-1234-6789")
                .build();

        ParticipantApplication findParticipantApplication1 = ParticipantApplication.builder()
                .participantApplicationId(1L)
                .participant(findParticipant1)
                .event(event)
                .eventTeamNumber(1)
                .build();

        ParticipantApplication findParticipantApplication3 = ParticipantApplication.builder()
                .participantApplicationId(3L)
                .participant(findParticipant3)
                .event(event)
                .eventTeamNumber(2)
                .build();

        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(participantRepository.findAllByUser_UserId(user.getUserId())).willReturn(Arrays.asList(findParticipant1, findParticipant2, findParticipant3));
        given(participantApplicationRepository.findByParticipant_ParticipantIdAndEvent_EventId(findParticipant1.getParticipantId(), SECOND_EVENT_ID)).willReturn(Optional.of(findParticipantApplication1));
        given(participantApplicationRepository.findByParticipant_ParticipantIdAndEvent_EventId(findParticipant2.getParticipantId(), SECOND_EVENT_ID)).willReturn(Optional.empty());
        given(participantApplicationRepository.findByParticipant_ParticipantIdAndEvent_EventId(findParticipant3.getParticipantId(), SECOND_EVENT_ID)).willReturn(Optional.of(findParticipantApplication3));

        //  when
        ResponseDto<?> responseDto = secondParticipantService.getSecondList(user.getUserId());

        //  then
        assertTrue(responseDto.getIsSuccess());
        assertTrue(((ParticipantResponseDto)responseDto.getPayload()).getIsParticipantExists());
        List<SecondParticipantResponseDto> secondResponseDtoList = ((ParticipantResponseDto)responseDto.getPayload()).getParticipants().getParticipants();
        assertEquals(secondResponseDtoList.size(), 2);

        verify(participantApplicationRepository, times((3))).findByParticipant_ParticipantIdAndEvent_EventId(anyLong(), anyLong());

    }

}
