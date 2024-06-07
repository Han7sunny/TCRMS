package com.kutca.tcrms.participant.controller;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.participant.controller.dto.request.TeamParticipantRequestDto;
import com.kutca.tcrms.participant.service.TeamParticipantService;
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
public class TeamParticipantController {

    private final TeamParticipantService teamParticipantService;

    @Operation(summary = "단체전 신청 확인(조회)")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/api/user/team")
    public ResponseEntity<?> getTeamList(@RequestParam Long userId){
        try {
            return new ResponseEntity<>(teamParticipantService.getTeamList(userId), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "단체전 신청")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @PostMapping("/api/user/team")
    public ResponseEntity<?> registTeamList(@RequestBody RequestDto<TeamParticipantRequestDto.Regist> teamParticipantRequestDto){
        try {
            return new ResponseEntity<>(teamParticipantService.registTeamList(teamParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "단체전 신청 수정")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @PostMapping("/api/user/team")
    public ResponseEntity<?> modifyTeam(@RequestBody TeamParticipantRequestDto.Modify teamParticipantRequestDto){
        try {
            return new ResponseEntity<>(teamParticipantService.modifyTeam(teamParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "단체전 신청 삭제")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @DeleteMapping("/api/user/team")
    public ResponseEntity<?> deleteTeam(@RequestBody TeamParticipantRequestDto.Delete teamParticipantRequestDto){
        try {
            return new ResponseEntity<>(teamParticipantService.deleteTeam(teamParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
