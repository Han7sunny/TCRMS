package com.kutca.tcrms.participant.controller;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.participant.controller.dto.request.SecondParticipantRequestDto;
import com.kutca.tcrms.participant.service.SecondParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SecondParticipantController {

    private final SecondParticipantService secondParticipantService;

    @Operation(summary = "세컨 신청 확인(조회)")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/api/user/second")
    public ResponseEntity<?> getSecondList(@RequestParam Long userId){
        try {
            return new ResponseEntity<>(secondParticipantService.getSecondList(userId), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "세컨 신청")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @PostMapping("/api/user/second")
    public ResponseEntity<?> registSecondList(@RequestBody RequestDto<SecondParticipantRequestDto.Regist> secondParticipantRequestDto){
        try {
            return new ResponseEntity<>(secondParticipantService.registSecondList(secondParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "세컨 신청 수정")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @PutMapping("/api/user/second")
    public ResponseEntity<?> modifySecond(@RequestBody SecondParticipantRequestDto.Modify secondParticipantRequestDto){
        try {
            return new ResponseEntity<>(secondParticipantService.modifySecond(secondParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "세컨 신청 삭제")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @DeleteMapping("/api/user/second")
    public ResponseEntity<?> deleteSecond(@RequestBody SecondParticipantRequestDto.Delete secondParticipantRequestDto){
        try {
            return new ResponseEntity<>(secondParticipantService.deleteSecond(secondParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
