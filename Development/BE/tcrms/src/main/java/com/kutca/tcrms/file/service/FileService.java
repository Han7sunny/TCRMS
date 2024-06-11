package com.kutca.tcrms.file.service;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.file.controller.dto.response.FileResponseDto;
import com.kutca.tcrms.file.controller.dto.response.FilesResponseDto;
import com.kutca.tcrms.file.repository.FileRepository;
import com.kutca.tcrms.participant.controller.dto.request.IndividualParticipantRequestDto;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    private final ParticipantRepository participantRepository;
    private final ParticipantApplicationRepository participantApplicationRepository;
    private final FileRepository fileRepository;

    public ResponseDto<?> uploadFileInfo(List<String> filePath, IndividualParticipantRequestDto.File participant){

        Long participantId = participant.getParticipantId();

        List<FileResponseDto> fileInfos = participant.getFileInfos().getFileInfos().stream().map(fileInfo -> {
            Long fileId = fileInfo.getFileId();
            String fileName = fileInfo.getFileName();

            //  filePath 동시에 작업 -> Map

            return FileResponseDto.builder().build();
        }).collect(Collectors.toList());

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(FilesResponseDto.builder()
                        .fileInfos(fileInfos)
                        .build())
                .build();
    }
}
