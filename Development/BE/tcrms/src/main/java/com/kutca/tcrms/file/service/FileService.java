package com.kutca.tcrms.file.service;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.event.repository.EventRepository;
import com.kutca.tcrms.file.controller.dto.request.FilesRequestDto;
import com.kutca.tcrms.file.controller.dto.response.FileResponseDto;
import com.kutca.tcrms.file.controller.dto.response.FilesResponseDto;
import com.kutca.tcrms.file.entity.File;
import com.kutca.tcrms.file.repository.FileRepository;
import com.kutca.tcrms.participant.controller.dto.request.IndividualParticipantRequestDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantFileResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantsResponseDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import com.kutca.tcrms.participantfile.entity.ParticipantFile;
import com.kutca.tcrms.participantfile.entity.ParticipantFileRepository;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class FileService {

    private final ParticipantRepository participantRepository;
    private final ParticipantApplicationRepository participantApplicationRepository;
    private final FileRepository fileRepository;
    private final ParticipantFileRepository participantFileRepository;

    public ResponseDto<?> isFileCompleted(Long userId){

        List<Participant> findParticipantList = participantRepository.findAllByUser_UserId(userId);
        if(findParticipantList.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("신청 내역이 존재하지 않습니다.")
                    .build();
        }

        boolean allFilesCompleted = true;

        for (Participant participant : findParticipantList){
            if(!participantFileRepository.findByParticipant_ParticipantId(participant.getParticipantId()).get().getIsAllFileCompleted()){
                allFilesCompleted = false;
                break;
            }
        }

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(allFilesCompleted)
                .build();

    }

    public ResponseDto<?> getFileInfoList (Long userId){

        List<Participant> findParticipantList = participantRepository.findAllByUser_UserId(userId);
        if(findParticipantList.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(true)
                    .message("신청 내역이 존재하지 않습니다.")
                    .build();
        }

        List<ParticipantFileResponseDto> participants = findParticipantList.stream().map(participant -> {

            ParticipantFile participantFile = participantFileRepository.findByParticipant_ParticipantId(participant.getParticipantId()).get();
            List<File> files = fileRepository.findAllByParticipant_ParticipantId(participant.getParticipantId());

            ParticipantFileResponseDto participantFileResponseDto = ParticipantFileResponseDto.builder()
                    .participantId(participant.getParticipantId())
                    .name(participant.getName())
                    .isForeigner(participant.getIsForeigner())
                    .identityNumber(participant.getIdentityNumber())
                    .fileInfos(files.stream().map(FileResponseDto::fromEntity).collect(Collectors.toList()))
                    .isAllFileConfirmed(participantFile.getIsAllFileCompleted())
                    .build();

            if (participantApplicationRepository.existsByParticipant_ParticipantIdAndEvent_EventId(participant.getParticipantId(), 11L)) {
                participantFileResponseDto.setTypes(List.of("자원봉사자"));
                return participantFileResponseDto;
            }

            List<String> events = participantApplicationRepository.findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(participant.getParticipantId(), 1L, 9L).stream().map(participantApplication -> participantApplication.getEvent().getEventName()).collect(Collectors.toList());
            List<String> types = new ArrayList<>();

            if (!events.isEmpty()) {
                types.add("선수");
            }

            if (participantApplicationRepository.existsByParticipant_ParticipantIdAndEvent_EventId(participant.getParticipantId(), 10L)) {
                types.add("세컨");
            }

            participantFileResponseDto.setTypes(types);
            participantFileResponseDto.setEvents(events);

            return participantFileResponseDto;

        }).toList();

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(ParticipantsResponseDto.builder()
                        .participants(new ArrayList<>(participants))
                        .build())
                .build();
    }

    public ResponseDto<?> uploadFileInfo(Long participantId, List<String> filePaths, FilesRequestDto fileInfos){

        Optional<Participant> findParticipant = participantRepository.findById(participantId);
        if(findParticipant.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("참가자 정보를 조회할 수 없습니다.")
                    .build();
        }

        Participant participant = findParticipant.get();
        ParticipantFile participantFile = participantFileRepository.findByParticipant_ParticipantId(participant.getParticipantId())
                .orElseGet(() -> participantFileRepository.save(ParticipantFile.builder()
                                .participant(participant)
                                .isAllFileCompleted(false)
                        .build()));

        List<FileResponseDto> filesResponseDto = IntStream.range(0, filePaths.size()).mapToObj(idx ->{
            Long fileId = fileInfos.getFileInfos().get(idx).getFileId();
            String fileName = fileInfos.getFileInfos().get(idx).getFileName();
            String filePath = filePaths.get(idx);

            File savedFile = (fileId != null)
                    ? fileRepository.findById(fileId).get().updateFilePath(filePath)
                    : fileRepository.save(File.builder()
                            .fileName(fileName)
                            .filePath(filePath)
                            .participantFile(participantFile)
                    .build());

            return FileResponseDto.builder()
                    .fileId(savedFile.getFileId())
                    .fileName(savedFile.getFileName())
                    .build();
        }).collect(Collectors.toList());

        int requiredFileCount = participant.getIsForeigner() ? 1 : 0;
        int submittedFileCount = fileRepository.countAllByParticipant_ParticipantId(participant.getParticipantId());

        //  선수 또는 세컨 신청 내역 존재할 경우 최소 4개
        if (participantApplicationRepository.existsByParticipant_ParticipantIdAndEvent_EventIdBetween(participant.getParticipantId(), 1L, 10L)){
            requiredFileCount += 4;
        }

        if (requiredFileCount == submittedFileCount) {
            participantFile.updateIsAllFileCompleted(true);
        }

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(FilesResponseDto.builder()
                        .fileInfos(filesResponseDto)
                        .build())
                .build();
    }
}
