package com.kutca.tcrms.participant;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.common.enums.Role;
import com.kutca.tcrms.participant.controller.dto.response.IndividualParticipantResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantResponseDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participant.service.ParticipantService;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
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

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
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

        //  추후 request participantsResponseDto로 수정
        Participant participant1 = Participant.builder()
                .name("이다빈")
                .identityNumber("961207-2345678")
                .gender("여성")
                .universityName(user.getUniversityName())
                .isForeigner(false)
                .nationality(null)
                .phoneNumber("010-1234-5678")
                .user(user)
//                        .weightClass()
                .build();


        Participant participant2 = Participant.builder()
                .name("Tom")
                .identityNumber("991010-1234567")
                .gender("남성")
                .universityName(user.getUniversityName())
                .isForeigner(true)
                .nationality("영국")
                .phoneNumber("010-1111-9876")
                .user(user)
//                        .weightClass()
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(participantRepository.findAllByUser_UserId(userId)).willReturn(Arrays.asList(participant1, participant2));

        //  when
        ResponseDto<?> responseDto = participantService.getIndividualList(userId);

        //  then
        assertTrue(responseDto.getIsSuccess());

        ParticipantResponseDto participantResponseDto = (ParticipantResponseDto)responseDto.getPayload();

        assertEquals(participantResponseDto.getIsEditable(), user.getIsEditable());
        assertNotEquals(participantResponseDto.getIsParticipantExists(), participantResponseDto.getParticipants().getParticipants().isEmpty());

        List<IndividualParticipantResponseDto> individualParticipantResponseDto = participantResponseDto.getParticipants().getParticipants();

        assertFalse(individualParticipantResponseDto.isEmpty());
        assertFalse(individualParticipantResponseDto.get(0).getIsForeigner());
//        assertNull(individualParticipantResponseDto.get(0).getNationality());  //  entity 설정에 default null로 설정해주기?
        assertEquals(individualParticipantResponseDto.get(0).getParticipantName(), participant1.getName());
        assertEquals(individualParticipantResponseDto.get(0).getIdentityNumber(), participant1.getIdentityNumber());

        assertTrue(individualParticipantResponseDto.get(1).getIsForeigner());
        assertEquals(individualParticipantResponseDto.get(1).getNationality(), participant2.getNationality());

    }
}
