package com.kutca.tcrms.participant;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.common.enums.Role;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.response.IndividualParticipantResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantResponseDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participant.service.ParticipantService;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ParticipantServiceTest {
    @InjectMocks
    private ParticipantService participantService;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WeightClassRepository weightClassRepository;

    @Mock
    private ParticipantApplicationRepository participantApplicationRepository;

    @Mock
    private EventRepository eventRepository;

//    @BeforeEach
//    void setUp() {
//    }

    @Test
    @DisplayName("개인전 신청 확인 성공")
    public void getIndividualList(){
        //  given
        Long userId = 1L;
        User user = User.builder()
                .userId(userId)
                .username("홍길동")
                .universityName("서울대학교")
                .password("1234")
                .auth(Role.USER)
                .depositorName("입금주명")
                .isFirstLogin(true)
                .isEditable(true)
                .isDepositConfirmed(false)
                .build();

        WeightClass weightClass1 = WeightClass.builder()
                .weightClassId(1L)
                .name("플라이")
                .build();

        WeightClass weightClass2 = WeightClass.builder()
                .weightClassId(2L)
                .name("라이트")
                .build();

        //  추후 request participantsResponseDto로 수정
        Participant participant1 = Participant.builder()
                .participantId(1L)
                .name("이다빈")
                .identityNumber("961207-2345678")
                .gender("여성")
                .universityName(user.getUniversityName())
                .isForeigner(false)
                .nationality(null)
                .phoneNumber("010-1234-5678")
                .user(user)
                .weightClass(weightClass1)
                .build();


        Participant participant2 = Participant.builder()
                .participantId(2L)
                .name("Tom")
                .identityNumber("991010-1234567")
                .gender("남성")
                .universityName(user.getUniversityName())
                .isForeigner(true)
                .nationality("영국")
                .phoneNumber("010-1111-9876")
                .user(user)
                .weightClass(weightClass2)
                .build();

        Event event1 = Event.builder().eventId(1L).eventName("개인전 여자 겨루기").build();
        Event event2 = Event.builder().eventId(2L).eventName("개인전 여자 품새").build();
        Event event3 = Event.builder().eventId(3L).eventName("개인전 남자 겨루기").build();
        Event event4 = Event.builder().eventId(4L).eventName("개인전 남자 품새").build();

        ParticipantApplication participantApplication1 = ParticipantApplication.builder()
                .participant(participant1)
                .event(event1)
                .build();

        ParticipantApplication participantApplication2 = ParticipantApplication.builder()
                .participant(participant1)
                .event(event2)
                .build();

        ParticipantApplication participantApplication3 = ParticipantApplication.builder()
                .participant(participant2)
                .event(event3)
                .build();


        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(participantRepository.findAllByUser_UserId(userId)).willReturn(Arrays.asList(participant1, participant2));
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participant1.getParticipantId(), event1.getEventId(), event4.getEventId())).willReturn(Arrays.asList(participantApplication1, participantApplication2));
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participant2.getParticipantId(), event1.getEventId(), event4.getEventId())).willReturn(Arrays.asList(participantApplication3));

        //  when
        ResponseDto<?> responseDto = participantService.getIndividualList(userId);

        //  then
        assertTrue(responseDto.getIsSuccess());

        ParticipantResponseDto participantResponseDto = (ParticipantResponseDto)responseDto.getPayload();

        assertEquals(participantResponseDto.getIsEditable(), user.getIsEditable());
        assertNotEquals(participantResponseDto.getIsParticipantExists(), participantResponseDto.getParticipants().getParticipants().isEmpty());

        List<IndividualParticipantResponseDto> individualParticipantResponseDto = participantResponseDto.getParticipants().getParticipants();

        assertFalse(individualParticipantResponseDto.get(0).getIsForeigner());
//        assertNull(individualParticipantResponseDto.get(0).getNationality());  //  entity 설정에 default null로 설정해주기?
        assertEquals(individualParticipantResponseDto.get(0).getParticipantName(), participant1.getName());
        assertEquals(individualParticipantResponseDto.get(0).getIdentityNumber(), participant1.getIdentityNumber());
        assertEquals(individualParticipantResponseDto.get(0).getEventIds().size(), 2);
        assertEquals(individualParticipantResponseDto.get(0).getWeightClassId(), weightClass1.getWeightClassId());

        assertTrue(individualParticipantResponseDto.get(1).getIsForeigner());
        assertEquals(individualParticipantResponseDto.get(1).getNationality(), participant2.getNationality());
        assertEquals(individualParticipantResponseDto.get(1).getEventIds().size(), 1);
        assertEquals(individualParticipantResponseDto.get(1).getWeightClassId(), weightClass2.getWeightClassId());

    }
}
