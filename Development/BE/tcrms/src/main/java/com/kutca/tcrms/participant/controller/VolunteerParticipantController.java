package com.kutca.tcrms.participant.controller;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.participant.controller.dto.request.VolunteerParticipantRequestDto;
import com.kutca.tcrms.participant.service.VolunteerParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "VolunteerParticipantController", description = "자원봉사자 신청 관련 Controller")
public class VolunteerParticipantController {

    private final VolunteerParticipantService volunteerParticipantService;

    @Operation(summary = "자원봉사자 신청 확인(조회)")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/api/user/volunteer")
    public ResponseEntity<?> getVolunteerList(@RequestParam Long userId) {
        try {
            return new ResponseEntity<>(volunteerParticipantService.getVolunteerList(userId), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "자원봉사자 신청")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @PostMapping("/api/user/volunteer")
    public ResponseEntity<?> registVolunteerList(@RequestBody RequestDto<VolunteerParticipantRequestDto.Regist> volunteerParticipantRequestDto) {
        try {
            return new ResponseEntity<>(volunteerParticipantService.registVolunteer(volunteerParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "자원봉사자 신청 수정")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @PutMapping("/api/user/volunteer")
    public ResponseEntity<?> modifyVolunteer(@RequestBody VolunteerParticipantRequestDto.Modify volunteerParticipantRequestDto) {
        try {
            return new ResponseEntity<>(volunteerParticipantService.modifyVolunteer(volunteerParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "자원봉사자 신청 삭제")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @DeleteMapping("/api/user/volunteer")
    public ResponseEntity<?> deleteVolunteer(@RequestBody VolunteerParticipantRequestDto.Delete volunteerParticipantRequestDto) {
        try {
            return new ResponseEntity<>(volunteerParticipantService.deleteVolunteer(volunteerParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
