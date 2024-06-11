package com.kutca.tcrms.file.controller;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.file.controller.dto.request.FilesRequestDto;
import com.kutca.tcrms.file.service.FileService;
import com.kutca.tcrms.participant.controller.dto.request.IndividualParticipantRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "참가자 정보 및 관련 서류 제출 확인 여부")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    public ResponseEntity<?> getFileInfoList(@RequestParam Long userId){
        try {
            return new ResponseEntity<>(fileService.getFileInfoList(userId), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "선수별 서류 제출")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @PostMapping("/api/user/file")
    public ResponseEntity<?> uploadFiles(@RequestParam MultipartFile[] files, @RequestParam Long participantId, @RequestParam FilesRequestDto fileInfos){
        try {
//            List<String> filePath = s3Service.uploadFileToS3(files);
            List<String> filePaths = null;
            return new ResponseEntity<>(fileService.uploadFileInfo(participantId, filePaths, fileInfos), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
