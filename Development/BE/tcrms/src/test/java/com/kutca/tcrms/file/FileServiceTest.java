package com.kutca.tcrms.file;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.file.controller.dto.response.FileResponseDto;
import com.kutca.tcrms.file.entity.File;
import com.kutca.tcrms.file.repository.FileRepository;
import com.kutca.tcrms.file.service.FileService;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantFileResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantsResponseDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private FileRepository fileRepository;

    @Mock
    private ParticipantFileRepository participantFileRepository;

    private User user;

    private Participant findParticipant1, findParticipant2, findParticipant3;

    private ParticipantApplication findParticipantApplication1, findParticipantApplication2, findParticipantApplication3;

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

        findParticipantFile1 = ParticipantFile.builder()
                .participantFileId(1L)
                .isAllFileCompleted(false)
                .build();

        Event event1 = Event.builder()
                .eventId(1L)
                .eventName("개인전 여자 겨루기")
                .build();

        findParticipantApplication1 = ParticipantApplication.builder()
                .participant(findParticipant1)
                .event(event1)
                .build();

        Event event10 = Event.builder().eventId(10L).eventName("세컨").build();

        findParticipantApplication2 = ParticipantApplication.builder()
                .participant(findParticipant1)
                .event(event10)
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

        findParticipantFile2 = ParticipantFile.builder()
                .participantFileId(2L)
                .isAllFileCompleted(true)
                .build();

        Event event11 = Event.builder().eventId(11L).eventName("자원봉사자").build();

        findParticipantApplication3 = ParticipantApplication.builder()
                .participant(findParticipant2)
                .event(event11)
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

    @Test
    @DisplayName("참가자 및 관련 서류 정보")
    void getFileInfoListSuccess(){

        //  given
        File findFile1 = File.builder()
                .fileId(1L)
                .participantFile(findParticipantFile1)
                .build();

        File findFile2 = File.builder()
                .fileId(2L)
                .participantFile(findParticipantFile1)
                .build();

        File findFile3 = File.builder()
                .fileId(3L)
                .participantFile(findParticipantFile1)
                .build();

//        File findFile4 = File.builder()
//                .fileId(4L)
//                .participantFile(findParticipantFile1)
//                .build();

        File findFile5 = File.builder()
                .fileId(5L)
                .participantFile(findParticipantFile2)
                .build();


        given(participantRepository.findAllByUser_UserId(user.getUserId())).willReturn(Arrays.asList(findParticipant1, findParticipant2));
        given(participantFileRepository.findByParticipant_ParticipantId(findParticipant1.getParticipantId())).willReturn(Optional.of(findParticipantFile1));
        given(fileRepository.findAllByParticipant_ParticipantId(findParticipant1.getParticipantId())).willReturn(Arrays.asList(findFile1, findFile2, findFile3));
        given(participantFileRepository.findByParticipant_ParticipantId(findParticipant2.getParticipantId())).willReturn(Optional.of(findParticipantFile2));
        given(fileRepository.findAllByParticipant_ParticipantId(findParticipant2.getParticipantId())).willReturn(Arrays.asList(findFile5));
        given(participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(findParticipant1.getParticipantId(), 1L, 9L)).willReturn(Arrays.asList(findParticipantApplication1, findParticipantApplication2));
        given(participantApplicationRepository.existsByParticipant_ParticipantIdAndEvent_EventId(findParticipant1.getParticipantId(), 11L)).willReturn(false);
        given(participantApplicationRepository.existsByParticipant_ParticipantIdAndEvent_EventId(findParticipant1.getParticipantId(), 10L)).willReturn(true);
        given(participantApplicationRepository.existsByParticipant_ParticipantIdAndEvent_EventId(findParticipant2.getParticipantId(), 11L)).willReturn(true);

        //  when
        ResponseDto<?> responseDto = fileService.getFileInfoList(user.getUserId());

        //  then
        assertTrue(responseDto.getIsSuccess());

        List<ParticipantFileResponseDto> participants = ((ParticipantsResponseDto)responseDto.getPayload()).getParticipants();

        assertEquals(participants.get(0).getTypes().size(), 2);
        assertEquals(participants.get(0).getEvents().size(), 2);
        assertEquals(participants.get(0).getTypes().get(0), "선수");
        assertEquals(participants.get(0).getTypes().get(1), "세컨");
        assertNotEquals(participants.get(0).getFileInfos().size(), 4);
        assertFalse(participants.get(0).getIsAllFileConfirmed());

        assertTrue(participants.get(1).getIsForeigner());
        assertEquals(participants.get(1).getTypes().size(), 1);
        assertEquals(participants.get(1).getTypes().get(0), "자원봉사자");
        assertEquals(participants.get(1).getFileInfos().size(), 1);
        assertTrue(participants.get(1).getIsAllFileConfirmed());
    }

}
