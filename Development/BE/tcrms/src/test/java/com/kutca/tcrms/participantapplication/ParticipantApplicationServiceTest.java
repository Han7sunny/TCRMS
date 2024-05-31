package com.kutca.tcrms.participantapplication;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.common.enums.Role;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.participant.controller.dto.request.IndividualParticipantRequestDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.participantapplication.service.ParticipantApplicationService;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.weightclass.entity.WeightClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ParticipantApplicationServiceTest {

    @InjectMocks
    private ParticipantApplicationService participantApplicationService;

    @Mock
    private ParticipantApplicationRepository participantApplicationRepository;

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

}
