package com.kutca.tcrms.participantapplication;

import com.kutca.tcrms.account.entity.Account;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.common.enums.Role;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.participant.controller.dto.request.IndividualParticipantRequestDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participantapplication.controller.dto.response.ParticipantApplicationResponseDto;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.participantapplication.service.ParticipantApplicationService;
import com.kutca.tcrms.secondperiod.entity.SecondPeriod;
import com.kutca.tcrms.secondperiod.repository.SecondPeriodRepository;
import com.kutca.tcrms.universityapplication.repository.UniversityApplicationRepository;
import com.kutca.tcrms.user.controller.dto.response.FinalSubmitResponseDto;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import com.kutca.tcrms.weightclass.entity.WeightClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ParticipantApplicationServiceTest {

    @InjectMocks
    private ParticipantApplicationService participantApplicationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private SecondPeriodRepository secondPeriodRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private ParticipantApplicationRepository participantApplicationRepository;

    @Mock
    private UniversityApplicationRepository universityApplicationRepository;

    private User kutca, findUser1, findUser2;
    private Account kutcaAccount;
    private SecondPeriod kutcaSecondPeriod;
    private Map<Long, Event> events = new HashMap<>();
    private Participant findParticipant1, findParticipant2, findParticipant3;
    private ParticipantApplication findParticipantApplication1, findParticipantApplication2, findParticipantApplication3, findParticipantApplication4, findParticipantApplication5, findParticipantApplication6, findParticipantApplication7, findParticipantApplication8;

    @BeforeEach
    void setUp(){
        kutca = User.builder()
                .userId(1L)
                .username("관리자")
                .universityName("서울대학교")
                .password("9999")
                .auth(Role.ADMIN)
                .depositorName("전국대학태권도동아리연합회")
                .isFirstLogin(true)
                .isEditable(true)
                .isDepositConfirmed(false)
                .build();

        findUser1 = User.builder()
                .userId(2L)
                .username("홍길동")
                .universityName("한국대학교")
                .password("1234")
                .auth(Role.USER)
                .depositorName("홍길동")
                .isFirstLogin(true)
                .isEditable(true)
                .isDepositConfirmed(false)
                .build();

        findUser2 = User.builder()
                .userId(3L)
                .username("홍길동")
                .universityName("대한대학교")
                .password("1234")
                .auth(Role.USER)
                .depositorName("홍길동")
                .isFirstLogin(true)
                .isEditable(true)
                .isDepositConfirmed(false)
                .build();

        kutcaAccount = Account.builder()
                .accountId(1L)
                .accountBank("한국은행")
                .accountNumber("111-111-1111111")
                .depositOwnerName("전국대학태권도동아리연합회")
                .build();

        kutcaSecondPeriod = SecondPeriod.builder()
                .user(kutca)
                .account(kutcaAccount)
                .build();

        events.put(1L, Event.builder().eventId(1L).eventName("개인전 여자 겨루기").eventFee(30000).build());
        events.put(2L, Event.builder().eventId(2L).eventName("개인전 여자 품새").eventFee(events.get(1L).getEventFee()).build());
        events.put(3L, Event.builder().eventId(3L).eventName("개인전 남자 겨루기").eventFee(events.get(1L).getEventFee()).build());
        events.put(4L, Event.builder().eventId(4L).eventName("개인전 남자 품새").eventFee(events.get(1L).getEventFee()).build());
        events.put(5L, Event.builder().eventId(5L).eventName("단체전 여자 겨루기").eventFee(20000).build());
        events.put(6L, Event.builder().eventId(6L).eventName("단체전 여자 품새").eventFee(20000).build());
        events.put(7L, Event.builder().eventId(7L).eventName("단체전 남자 겨루기").eventFee(30000).build());
        events.put(8L, Event.builder().eventId(8L).eventName("단체전 남자 품새").eventFee(30000).build());
        events.put(9L, Event.builder().eventId(9L).eventName("단체전 혼성 품새").eventFee(30000).build());

        findParticipant1 = Participant.builder()
                .participantId(1L)
                .user(findUser1)
                .name("성춘향")
                .gender("여성")
                .identityNumber("001004-45671238")
                .isForeigner(false)
                .universityName(findUser1.getUniversityName())
//                .weightClass(weightClassMap.get(1L))
                .build();

        findParticipantApplication1 = ParticipantApplication.builder()
                .participantApplicationId(1L)
                .participant(findParticipant1)
                .event(events.get(1L))
                .build();

        findParticipantApplication2 = ParticipantApplication.builder()
                .participantApplicationId(2L)
                .participant(findParticipant1)
                .event(events.get(2L))
                .build();

        findParticipantApplication3 = ParticipantApplication.builder()
                .participantApplicationId(3L)
                .participant(findParticipant1)
                .event(events.get(5L))
                .eventTeamNumber(1)
                .build();

        findParticipantApplication8 = ParticipantApplication.builder()
                .participantApplicationId(8L)
                .participant(findParticipant1)
                .event(events.get(9L))
                .eventTeamNumber(1)
                .build();

        findParticipant2 = Participant.builder()
                .participantId(2L)
                .user(findUser1)
                .name("Ken")
                .gender("남성")
//                .identityNumber("001004-45671238")
                .isForeigner(true)
                .nationality("미국")
                .universityName(findUser1.getUniversityName())
//                .weightClass(weightClassMap.get(1L))
                .build();

        findParticipantApplication4 = ParticipantApplication.builder()
                .participantApplicationId(4L)
                .participant(findParticipant2)
                .event(events.get(3L))
                .build();

        findParticipantApplication5 = ParticipantApplication.builder()
                .participantApplicationId(5L)
                .participant(findParticipant2)
                .event(events.get(7L))
                .eventTeamNumber(1)
                .build();

        findParticipantApplication6 = ParticipantApplication.builder()
                .participantApplicationId(6L)
                .participant(findParticipant2)
                .event(events.get(7L))
                .eventTeamNumber(2)
                .build();

        findParticipantApplication7 = ParticipantApplication.builder()
                .participantApplicationId(7L)
                .participant(findParticipant2)
                .event(events.get(9L))
                .eventTeamNumber(1)
                .build();
    }

    @Test
    @DisplayName("개인전 신청 삭제 성공")
    public void deleteIndividualParticipant(){

        //  given
        User user = User.builder()
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

        WeightClass weightClass = WeightClass.builder()
                .weightClassId(1L)
                .name("플라이")
                .build();

        Participant participant = Participant.builder()
                .participantId(1L)
                .name("이다빈")
                .identityNumber("961207-2345678")
                .gender("여성")
                .universityName(user.getUniversityName())
                .isForeigner(false)
                .nationality(null)
                .phoneNumber("010-1234-5678")
                .user(user)
                .weightClass(weightClass)
                .build();

        IndividualParticipantRequestDto.Delete individualParticipantRequestDto = IndividualParticipantRequestDto.Delete.builder()
                .userId(user.getUserId())
                .participantId(participant.getParticipantId())
                .participantApplicationIds(Arrays.asList(1L, 2L))
                .build();

        //  when
        ResponseDto<?> responseDto = participantApplicationService.deleteParticipantApplication(individualParticipantRequestDto);

        //  then
        assertTrue(responseDto.getIsSuccess());
        assertEquals(participantApplicationRepository.findAllByParticipant_ParticipantId(participant.getParticipantId()), new ArrayList<>());
        assertEquals(participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participant.getParticipantId(), 1L, 4L), new ArrayList<>());
    }

    @Test
    @DisplayName("1차 참가비 정보 조회 성공")
    void getFirstPeriodParticipantApplicationFeeInfoSuccess(){

        //  given
        given(userRepository.findById(findUser1.getUserId())).willReturn(Optional.of(findUser1));
        given(secondPeriodRepository.findByUser_UserId(kutca.getUserId())).willReturn(Optional.of(kutcaSecondPeriod));
        given(eventRepository.findById(1L)).willReturn(Optional.of(events.get(1L)));
        given(eventRepository.findById(5L)).willReturn(Optional.of(events.get(5L)));
        given(eventRepository.findById(6L)).willReturn(Optional.of(events.get(6L)));
        given(eventRepository.findById(7L)).willReturn(Optional.of(events.get(7L)));
        given(eventRepository.findById(8L)).willReturn(Optional.of(events.get(8L)));
        given(eventRepository.findById(9L)).willReturn(Optional.of(events.get(9L)));

        given(participantRepository.findAllByUser_UserId(findUser1.getUserId())).willReturn(Arrays.asList(findParticipant1, findParticipant2));

        given(participantApplicationRepository.countAllByParticipant_ParticipantIdAndEvent_EventIdBetween(findParticipant1.getParticipantId(), 1L, 2L)).willReturn(2);
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(findParticipant1.getParticipantId(), 5L)).willReturn(Collections.singletonList(findParticipantApplication3));
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(findParticipant1.getParticipantId(), 6L)).willReturn(Collections.emptyList());
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(findParticipant1.getParticipantId(), 9L)).willReturn(Arrays.asList(findParticipantApplication8));

        given(participantApplicationRepository.countAllByParticipant_ParticipantIdAndEvent_EventIdBetween(findParticipant2.getParticipantId(), 3L, 4L)).willReturn(1);
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(findParticipant2.getParticipantId(), 7L)).willReturn(Arrays.asList(findParticipantApplication5, findParticipantApplication6));
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(findParticipant2.getParticipantId(), 8L)).willReturn(Collections.emptyList());
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndAndEvent_EventId(findParticipant2.getParticipantId(), 9L)).willReturn(Collections.singletonList(findParticipantApplication7));

        //  when
        ResponseDto<?> responseDto = participantApplicationService.getFirstPeriodParticipantApplicationFeeInfo(findUser1.getUserId());

        //  then
        assertTrue(responseDto.getIsSuccess());

        FinalSubmitResponseDto.FirstPeriod firstPeriodResponseDto = (FinalSubmitResponseDto.FirstPeriod) responseDto.getPayload();
        List<ParticipantApplicationResponseDto.FirstPeriod> firstPeriodParticipantApplicationInfos = firstPeriodResponseDto.getParticipantApplicationInfos().getParticipantApplicationInfos();

        assertEquals(firstPeriodParticipantApplicationInfos.size(), 4);
        assertEquals(firstPeriodParticipantApplicationInfos.get(0).getEventName(), "개인전");
        assertEquals(firstPeriodParticipantApplicationInfos.get(0).getParticipantCount(), 3);
        assertEquals(firstPeriodParticipantApplicationInfos.get(1).getEventName(), "겨루기 단체전");
        assertEquals(firstPeriodParticipantApplicationInfos.get(1).getParticipantCount(), 3);
        assertEquals(firstPeriodParticipantApplicationInfos.get(2).getEventName(), "품새 단체전");
        assertEquals(firstPeriodParticipantApplicationInfos.get(2).getParticipantCount(), 0);
        assertEquals(firstPeriodParticipantApplicationInfos.get(3).getEventName(), "품새 페어");
        assertEquals(firstPeriodParticipantApplicationInfos.get(3).getParticipantCount(), 1);

        assertEquals(firstPeriodResponseDto.getUserInfo().getUserName(), findUser1.getUsername());
        assertEquals(firstPeriodResponseDto.getUserInfo().getUniversityName(), findUser1.getUniversityName());

        assertEquals(firstPeriodResponseDto.getAccountInfo().getDepositOwnerName(), kutcaAccount.getDepositOwnerName());
        assertEquals(firstPeriodResponseDto.getAccountInfo().getAccountBank(), kutcaAccount.getAccountBank());
        assertEquals(firstPeriodResponseDto.getAccountInfo().getAccountNumber(), kutcaAccount.getAccountNumber());

    }

}
