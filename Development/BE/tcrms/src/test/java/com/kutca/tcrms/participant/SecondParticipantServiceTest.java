package com.kutca.tcrms.participant;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.SecondParticipantRequestDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantsResponseDto;
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
import static org.mockito.ArgumentMatchers.*;
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
    void setUp(){
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

    @Test
    @DisplayName("기존에 참가자 정보 존재하는 세컨 신청 성공")
    void registSecondListWithExistParticipant(){

        //  given
        SecondParticipantRequestDto.Regist second1 = SecondParticipantRequestDto.Regist.builder()
                .name("이몽룡")
                .gender("남성")
                .isForeigner(false)
                .identityNumber("981007-1234567")
                .build();

        SecondParticipantRequestDto.Regist second2 = SecondParticipantRequestDto.Regist.builder()
                .name("Sara")
                .gender("여성")
                .isForeigner(true)
                .nationality("미국")
                .identityNumber("981007-1234567")
                .build();

        SecondParticipantRequestDto.Regist second3 = SecondParticipantRequestDto.Regist.builder()
                .name("Tom")
                .gender("남성")
                .isForeigner(true)
                .nationality("영국")
                .phoneNumber("luckyTomy@gmail.com")
                .build();

        RequestDto<SecondParticipantRequestDto.Regist> registRequestDto = RequestDto.<SecondParticipantRequestDto.Regist>builder()
                .userId(user.getUserId())
                .requestDtoList(Arrays.asList(second1, second2, second3))
                .build();

        Participant findParticipant1 = Participant.builder()
                .participantId(1L)
                .name(second1.getName())
                .gender(second1.getGender())
                .isForeigner(second1.getIsForeigner())
                .nationality(second1.getNationality())
                .identityNumber(second1.getIdentityNumber())
                .phoneNumber(second1.getPhoneNumber())
                .build();

        Participant findParticipant2 = Participant.builder()
                .participantId(2L)
                .name(second2.getName())
                .gender(second2.getGender())
                .isForeigner(second2.getIsForeigner())
                .nationality(second2.getNationality())
                .identityNumber(second2.getIdentityNumber())
                .phoneNumber(second2.getPhoneNumber())
                .build();

        Participant savedParticipant1 = Participant.builder()
                .participantId(3L)
                .name(second3.getName())
                .gender(second3.getGender())
                .isForeigner(second3.getIsForeigner())
                .nationality(second3.getNationality())
                .identityNumber(second3.getIdentityNumber())
                .phoneNumber(second3.getPhoneNumber())
                .build();

        ParticipantApplication findParticipantApplication1 = ParticipantApplication.builder()
                .participantApplicationId(2L)
                .participant(findParticipant1)
                .eventTeamNumber(2)
                .build();

        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(eventRepository.findById(SECOND_EVENT_ID)).willReturn(Optional.of(event));
        given(participantApplicationRepository.findTopByEvent_EventId(SECOND_EVENT_ID)).willReturn(Optional.of(findParticipantApplication1));
        given(participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), second1.getName(), second1.getIdentityNumber())).willReturn(Optional.of(findParticipant1));
        given(participantRepository.findByUser_UserIdAndNameAndIdentityNumber(user.getUserId(), second2.getName(), second2.getIdentityNumber())).willReturn(Optional.of(findParticipant2)); // 외국인인데 identityNumber있을 수 있음
        given(participantRepository.findByUser_UserIdAndNameAndPhoneNumber(user.getUserId(), second3.getName(), second3.getPhoneNumber())).willReturn(Optional.empty());
        given(participantRepository.save(any(Participant.class))).willReturn(savedParticipant1);

        //  when
        ResponseDto<?> responseDto = secondParticipantService.registSecondList(registRequestDto);

        //  then
        assertTrue(responseDto.getIsSuccess());

        verify(participantRepository, times(2)).findByUser_UserIdAndNameAndIdentityNumber(anyLong(), anyString(), anyString());
        verify(participantRepository, times(1)).findByUser_UserIdAndNameAndPhoneNumber(anyLong(), anyString(), anyString());
        verify(participantRepository, times(1)).save(any(Participant.class));
        verify(participantApplicationRepository, times(registRequestDto.getRequestDtoList().size())).save(any(ParticipantApplication.class));

    }

    @Test
    @DisplayName("세컨 신청 수정 성공")
    void modifySecond(){

        //  given
        SecondParticipantRequestDto.Modify modifyRequestDto = SecondParticipantRequestDto.Modify.builder()
                .participantId(1L)
                .participantApplicationId(1L)
                .name("이몽룡")
                .gender("남성")
                .isForeigner(false)
                .identityNumber("981007-1234567")
                .build();

        Participant findParticipant1 = Participant.builder()
                .participantId(modifyRequestDto.getParticipantId())
                .name("이동룡")
                .gender(modifyRequestDto.getGender())
                .isForeigner(modifyRequestDto.getIsForeigner())
                .nationality(modifyRequestDto.getNationality())
                .identityNumber("980706-5432198")
                .phoneNumber(modifyRequestDto.getPhoneNumber())
                .build();

        Participant savedParticipant1 = Participant.builder()
                .participantId(modifyRequestDto.getParticipantId())
                .name(modifyRequestDto.getName())
                .gender(modifyRequestDto.getGender())
                .isForeigner(modifyRequestDto.getIsForeigner())
                .nationality(modifyRequestDto.getNationality())
                .identityNumber(modifyRequestDto.getIdentityNumber())
                .phoneNumber(modifyRequestDto.getPhoneNumber())
                .build();

        given(participantRepository.findById(modifyRequestDto.getParticipantId())).willReturn(Optional.of(findParticipant1));
        given(participantRepository.save(any(Participant.class))).willReturn(savedParticipant1);

        //  when
        ResponseDto<?> requestDto = secondParticipantService.modifySecond(modifyRequestDto);

        //  then
        assertTrue(requestDto.getIsSuccess());
        assertEquals(((SecondParticipantResponseDto)requestDto.getPayload()).getName(), modifyRequestDto.getName());
        assertEquals(((SecondParticipantResponseDto)requestDto.getPayload()).getIdentityNumber(), modifyRequestDto.getIdentityNumber());

        verify(eventRepository, times(0)).findById(anyLong());
        verify(participantRepository, times(1)).save(any(Participant.class));
    }

}
