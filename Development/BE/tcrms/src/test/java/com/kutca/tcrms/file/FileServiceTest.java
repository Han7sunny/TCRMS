package com.kutca.tcrms.file;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.file.controller.dto.response.FileResponseDto;
import com.kutca.tcrms.file.service.FileService;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.participantfile.entity.ParticipantFile;
import com.kutca.tcrms.participantfile.entity.ParticipantFileRepository;
import com.kutca.tcrms.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private ParticipantApplicationRepository participantApplicationRepository;

    @Mock
    private ParticipantFileRepository participantFileRepository;

    private User user;

    private Participant findParticipant1, findParticipant2, findParticipant3;

    private ParticipantFile findParticipantFile1, findParticipantFile2, findParticipantFile3;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .userId(1L)
                .universityName("서울대학교")
                .build();

        findParticipant1 = Participant.builder()
                .participantId(1L)
                .name("성춘향")
                .gender("여성")
                .identityNumber("001004-45671238")
                .isForeigner(false)
//                .weightClass(weightClassMap.get(1L))
                .build();

        findParticipant2 = Participant.builder()
                .participantId(2L)
                .name("Jennifer")
                .gender("여성")
                .isForeigner(true)
                .nationality("미국")
                .phoneNumber("kakao@gmail.com")
//                .weightClass(weightClassMap.get(2L))
                .build();

        findParticipant3 = Participant.builder()
                .participantId(3L)
                .name("Ken")
                .gender("남성")
                .isForeigner(true)
                .nationality("미국")
                .phoneNumber("kenny@gmail.com")
//                .weightClass(weightClassMap.get(3L))
                .build();

    }

    @Test
    @DisplayName("임금 및 최종 제출 메뉴 접근을 위한 모든 서류 제출 완료한 경우")
    void isAllFileCompletedSuccess(){

        //  given
        findParticipantFile1 = ParticipantFile.builder()
                .participantFileId(1L)
                .participant(findParticipant1)
                .isAllFileCompleted(true)
                .build();

        findParticipantFile2 = ParticipantFile.builder()
                .participantFileId(2L)
                .participant(findParticipant2)
                .isAllFileCompleted(true)
                .build();

        findParticipantFile3 = ParticipantFile.builder()
                .participantFileId(3L)
                .participant(findParticipant3)
                .isAllFileCompleted(true)
                .build();

        given(participantRepository.findAllByUser_UserId(user.getUserId())).willReturn(Arrays.asList(findParticipant1, findParticipant2, findParticipant3));
        given(participantFileRepository.existsByParticipant_ParticipantIdAndIsAllFileCompletedFalse(findParticipant1.getParticipantId())).willReturn(!findParticipantFile1.getIsAllFileCompleted());
        given(participantFileRepository.existsByParticipant_ParticipantIdAndIsAllFileCompletedFalse(findParticipant2.getParticipantId())).willReturn(!findParticipantFile2.getIsAllFileCompleted());
        given(participantFileRepository.existsByParticipant_ParticipantIdAndIsAllFileCompletedFalse(findParticipant3.getParticipantId())).willReturn(!findParticipantFile3.getIsAllFileCompleted());

        //  when
        ResponseDto<?> responseDto = fileService.isFileCompleted(user.getUserId());

        //  then
        assertTrue(responseDto.getIsSuccess());
        assertTrue(((FileResponseDto.Status)responseDto.getPayload()).isFileCompleted());

    }

    @Test
    @DisplayName("임금 및 최종 제출 메뉴 접근을 위한 모든 서류 제출 완료하지 않은 경우")
    void isAllFileNotCompletedSuccess(){

        //  given
        findParticipantFile1 = ParticipantFile.builder()
                .participantFileId(1L)
                .participant(findParticipant1)
                .isAllFileCompleted(true)
                .build();

        findParticipantFile2 = ParticipantFile.builder()
                .participantFileId(2L)
                .participant(findParticipant2)
                .isAllFileCompleted(true)
                .build();

        findParticipantFile3 = ParticipantFile.builder()
                .participantFileId(3L)
                .participant(findParticipant3)
                .isAllFileCompleted(false)
                .build();


        given(participantRepository.findAllByUser_UserId(user.getUserId())).willReturn(Arrays.asList(findParticipant1, findParticipant2, findParticipant3));
        given(participantFileRepository.existsByParticipant_ParticipantIdAndIsAllFileCompletedFalse(findParticipant1.getParticipantId())).willReturn(!findParticipantFile1.getIsAllFileCompleted());
        given(participantFileRepository.existsByParticipant_ParticipantIdAndIsAllFileCompletedFalse(findParticipant2.getParticipantId())).willReturn(!findParticipantFile2.getIsAllFileCompleted());
        given(participantFileRepository.existsByParticipant_ParticipantIdAndIsAllFileCompletedFalse(findParticipant3.getParticipantId())).willReturn(!findParticipantFile3.getIsAllFileCompleted());

        //  when
        ResponseDto<?> responseDto = fileService.isFileCompleted(user.getUserId());

        //  then
        assertTrue(responseDto.getIsSuccess());
        assertFalse(((FileResponseDto.Status)responseDto.getPayload()).isFileCompleted());

    }

}
