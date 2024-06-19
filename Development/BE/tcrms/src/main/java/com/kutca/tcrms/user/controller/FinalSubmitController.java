package com.kutca.tcrms.user.controller;

import com.kutca.tcrms.account.service.AccountService;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.participantapplication.service.ParticipantApplicationService;
import com.kutca.tcrms.universityapplication.service.UniversityApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "FinalSubmitController")
public class FinalSubmitController {

    private final AccountService accountService;
    private final UniversityApplicationService universityApplicationService;
    private final ParticipantApplicationService participantApplicationService;

    @Operation(summary = "참가비 정보 조회")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/api/user/final-submit/cost-info")
    public ResponseEntity<?> getParticipantApplicationFeeInfo(@RequestParam Long userId){
        try {
            return new ResponseEntity<>(participantApplicationService.getParticipantApplicationFeeInfo(userId), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "입금 정보 조회")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/api/user/final-submit/bank-info")
    public ResponseEntity<?> getDepositAccountInfo(@RequestParam Long userId){
        try {
            return new ResponseEntity<>(accountService.getDepositAccountInfo(userId), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "최종 제출 여부 조회")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/api/user/final-submit/is-final-submit")
    public ResponseEntity<?> isFinalSubmitConfirmed(@RequestParam Long userId){
        try {
            return new ResponseEntity<>(universityApplicationService.isFinalSubmitConfirmed(userId), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
