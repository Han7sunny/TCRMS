package com.kutca.tcrms.participant;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.common.enums.Role;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.IndividualParticipantRequestDto;
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

import java.util.*;

import static java.util.stream.DoubleStream.builder;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    private User user;

    private Map<Long, Event> events = new HashMap<>();

    @BeforeEach
    public void setUp() {

        user = User.builder()
                .userId(1L)
                .username("홍길동")
                .universityName("서울대학교")
                .password("1234")
                .auth(Role.USER)
                .depositorName("입금주명")
                .isFirstLogin(true)
                .isEditable(true)
                .isDepositConfirmed(false)
                .build();

        events.put(1L, Event.builder().eventId(1L).eventName("개인전 여자 겨루기").build());
        events.put(2L, Event.builder().eventId(2L).eventName("개인전 여자 품새").build());
        events.put(3L, Event.builder().eventId(3L).eventName("개인전 남자 겨루기").build());
        events.put(4L, Event.builder().eventId(4L).eventName("개인전 남자 품새").build());

    }

    @Test
    @DisplayName("개인전 신청 확인 성공")
    public void getIndividualList(){
        //  given
        Long userId = 1L;


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

        ParticipantApplication participantApplication1 = ParticipantApplication.builder()
                .participant(participant1)
                .event(events.get(1L))
                .build();

        ParticipantApplication participantApplication2 = ParticipantApplication.builder()
                .participant(participant1)
                .event(events.get(2L))
                .build();

        ParticipantApplication participantApplication3 = ParticipantApplication.builder()
                .participant(participant2)
                .event(events.get(3L))
                .build();


        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(participantRepository.findAllByUser_UserId(userId)).willReturn(Arrays.asList(participant1, participant2));
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participant1.getParticipantId(), events.get(1L).getEventId(), events.get(4L).getEventId())).willReturn(Arrays.asList(participantApplication1, participantApplication2));
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participant2.getParticipantId(), events.get(1L).getEventId(), events.get(4L).getEventId())).willReturn(Arrays.asList(participantApplication3));

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
        assertEquals(individualParticipantResponseDto.get(0).getName(), participant1.getName());
        assertEquals(individualParticipantResponseDto.get(0).getIdentityNumber(), participant1.getIdentityNumber());
        assertEquals(individualParticipantResponseDto.get(0).getEventInfo().size(), 2);
        assertEquals(individualParticipantResponseDto.get(0).getWeightClassId(), weightClass1.getWeightClassId());

        assertTrue(individualParticipantResponseDto.get(1).getIsForeigner());
        assertEquals(individualParticipantResponseDto.get(1).getNationality(), participant2.getNationality());
        assertEquals(individualParticipantResponseDto.get(1).getEventInfo().size(), 1);
        assertEquals(individualParticipantResponseDto.get(1).getWeightClassId(), weightClass2.getWeightClassId());

    }

    @Test
    @DisplayName("개인전 신청 등록 성공")
    public void registIndividualList(){

        //  given

        Event event1 = Event.builder().eventId(1L).eventName("개인전 여자 겨루기").build();
        Event event2 = Event.builder().eventId(2L).eventName("개인전 여자 품새").build();
        Event event3 = Event.builder().eventId(3L).eventName("개인전 남자 겨루기").build();
        Event event4 = Event.builder().eventId(4L).eventName("개인전 남자 품새").build();

        IndividualParticipantRequestDto.Regist participant1 = IndividualParticipantRequestDto.Regist.builder()
                .name("이다빈")
                .gender("여성")
                .isForeigner(false)
                .identityNumber("961207-2345678")
                .eventIds(Arrays.asList(event1.getEventId(), event2.getEventId()))
                .weightClassId(1L)
                .build();

        IndividualParticipantRequestDto.Regist participant2 = IndividualParticipantRequestDto.Regist.builder()
                .name("Tom")
                .gender("남성")
                .isForeigner(true)
                .nationality("영국")
                .identityNumber("991010-1234567")
                .eventIds(Arrays.asList(event3.getEventId()))
                .weightClassId(2L)
                .build();

        IndividualParticipantRequestDto.Regist participant3 = IndividualParticipantRequestDto.Regist.builder()
                .name("janja garnbret")
                .gender("여성")
                .isForeigner(true)
                .nationality("슬로베니아")
                .phoneNumber("386-123-4567-8900")
                .eventIds(Arrays.asList(event2.getEventId()))
                .build();

        RequestDto<IndividualParticipantRequestDto.Regist> registRequestDto = RequestDto.<IndividualParticipantRequestDto.Regist>builder()
                .userId(user.getUserId())
                .requestDtoList(Arrays.asList(participant1,participant2, participant3))
                .build();

        Participant savedParticipant1 = Participant.builder()
                .participantId(1L)
                .name(participant1.getName())
                .identityNumber(participant1.getIdentityNumber())
                .gender(participant1.getGender())
                .universityName(user.getUniversityName())
                .isForeigner(participant1.getIsForeigner())
                .nationality(null)
                .user(user)
                .weightClass(WeightClass.builder().weightClassId(participant1.getWeightClassId()).build())
                .build();

        Participant savedParticipant2 = Participant.builder()
                .participantId(2L)
                .name(participant2.getName())
                .identityNumber(participant2.getIdentityNumber())
                .gender(participant2.getGender())
                .universityName(user.getUniversityName())
                .isForeigner(participant2.getIsForeigner())
                .nationality(participant2.getNationality())
                .user(user)
                .weightClass(WeightClass.builder().weightClassId(participant2.getWeightClassId()).build())
                .build();

        Participant savedParticipant3 = Participant.builder()
                .participantId(3L)
                .name(participant3.getName())
                .gender(participant3.getGender())
                .universityName(user.getUniversityName())
                .isForeigner(participant3.getIsForeigner())
                .nationality(participant3.getNationality())
                .phoneNumber(participant3.getPhoneNumber())
                .user(user)
                .weightClass(null)
                .build();

        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(eventRepository.findAllByEventIdBetween(event1.getEventId(), event4.getEventId())).willReturn(Arrays.asList(event1, event2, event3, event4));
        given(participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), participant1.getName(), participant1.getIdentityNumber()))
                .willReturn(Optional.of(savedParticipant1));
        given(participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), participant2.getName(), participant2.getIdentityNumber()))
                .willReturn(Optional.of(savedParticipant2));
        given(participantRepository.findByUser_UserIdAndNameAndPhoneNumber(user.getUserId(), participant3.getName(), participant3.getPhoneNumber()))
                .willReturn(Optional.of(savedParticipant3));
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(savedParticipant1.getParticipantId(), 1L, 4L))
                .willReturn(Arrays.asList(
                        ParticipantApplication.builder().participant(savedParticipant1).event(event1).build(),
                        ParticipantApplication.builder().participant(savedParticipant1).event(event2).build()
                ));
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(savedParticipant2.getParticipantId(), 1L, 4L))
                .willReturn(Arrays.asList(
                        ParticipantApplication.builder().participant(savedParticipant2).event(event3).build()
                ));
        given(participantApplicationRepository.findTopByEvent_EventId(anyLong()))
                .willAnswer(event -> {
                    Long eventId = event.getArgument(0);
                    return Arrays.asList(
                                    ParticipantApplication.builder().participantApplicationId(1L).participant(savedParticipant1).event(event1).build(),
                                    ParticipantApplication.builder().participantApplicationId(2L).participant(savedParticipant1).event(event2).build(),
                                    ParticipantApplication.builder().participantApplicationId(3L).participant(savedParticipant2).event(event3).build(),
                                    ParticipantApplication.builder().participantApplicationId(4L).participant(savedParticipant3).event(event2).build()
                            ).stream()
                            .filter(pa -> pa.getEvent().getEventId().equals(eventId))
                            .max(Comparator.comparingLong(ParticipantApplication::getParticipantApplicationId));
                });

        //  when
        participantService.registIndividualList(registRequestDto);

        //  then
        Optional<Participant> findParticipant1 = participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), participant1.getName(), participant1.getIdentityNumber());
        assertNotNull(findParticipant1.get());
        assertEquals(findParticipant1.get().getUser().getUserId(), user.getUserId());
        assertNotNull(findParticipant1.get().getWeightClass());
        assertEquals(participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(findParticipant1.get().getParticipantId(), 1L, 4L).size(), participant1.getEventIds().size());

        Optional<Participant> findParticipant2 = participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), participant2.getName(), participant2.getIdentityNumber());
        assertNotNull(findParticipant2);
        assertEquals(findParticipant2.get().getUniversityName(), user.getUniversityName());
        assertEquals(participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(findParticipant2.get().getParticipantId(), 1L, 4L).size(), participant2.getEventIds().size());

        Optional<Participant> findParticipant3 = participantRepository.findByUser_UserIdAndNameAndPhoneNumber(user.getUserId(), participant3.getName(), participant3.getPhoneNumber());
        assertNotNull(findParticipant3);
        assertNull(findParticipant3.get().getWeightClass());
        assertEquals(participantApplicationRepository.findTopByEvent_EventId(participant3.getEventIds().get(0)).get().getParticipant().getParticipantId(), findParticipant3.get().getParticipantId());
    }

    @Test
    @DisplayName("개인전 신청 개인정보(이름) 수정 성공")
    public void modifyIndividualSuccess(){

        //  given
        IndividualParticipantRequestDto.Modify modifyRequestDto = IndividualParticipantRequestDto.Modify.builder()
                .isParticipantChange(true)
                .participantId(1L)
                .name("Tom")
                .gender("남자")
                .isForeigner(true)
                .nationality("영국")
                .identityNumber("981004-1234567")
                .isEventChange(false)
                .isWeightClassChange(false)
                .build();

        Participant savedParticipant = Participant.builder()
                .participantId(modifyRequestDto.getParticipantId())
                .name("Tomy")
                .identityNumber(modifyRequestDto.getIdentityNumber())
                .gender(modifyRequestDto.getGender())
                .universityName(user.getUniversityName())
                .isForeigner(modifyRequestDto.getIsForeigner())
                .nationality(modifyRequestDto.getNationality())
                .build();

        given(participantRepository.findById(modifyRequestDto.getParticipantId())).willReturn(Optional.of(savedParticipant));
        given(participantRepository.save(any(Participant.class))).willReturn(savedParticipant);

        //  when
        ResponseDto<?> responseDto = participantService.modifyIndividual(modifyRequestDto);

        //  then
        assertTrue(responseDto.getIsSuccess());
        assertEquals(((IndividualParticipantResponseDto)responseDto.getPayload()).getParticipantId(), modifyRequestDto.getParticipantId());
        assertEquals(((IndividualParticipantResponseDto) responseDto.getPayload()).getName(), modifyRequestDto.getName());

        verify(participantRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(0)).findById(anyLong());
        verify(weightClassRepository, times(0)).findById(anyLong());
        verify(participantApplicationRepository, times(0)).findTopByEvent_EventId(anyLong());

    }
}
