package com.kutca.tcrms.user.controller;

import com.kutca.tcrms.account.service.AccountService;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.participantapplication.service.ParticipantApplicationService;
import com.kutca.tcrms.universityapplication.service.UniversityApplicationService;
import com.kutca.tcrms.user.controller.dto.request.FinalSubmitRequestDto;
import com.kutca.tcrms.user.controller.dto.request.UserRequestDto;
import com.kutca.tcrms.user.service.UserService;
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
@Tag(name = "FinalSubmitController")
public class FinalSubmitController {

    private final UserService userService;
    private final AccountService accountService;
    private final UniversityApplicationService universityApplicationService;
    private final ParticipantApplicationService participantApplicationService;

    @Operation(summary = "1차 기간 참가비 정보 조회")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/api/user/first-period-final-submit/cost-info")
    public ResponseEntity<?> getParticipantApplicationFeeInfo(@RequestParam Long userId){
        try {
            return new ResponseEntity<>(participantApplicationService.getFirstPeriodParticipantApplicationFeeInfo(userId), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "대표자 정보 조회")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/api/user/final-submit/user-info")
    public ResponseEntity<?> getUserInfo(@RequestParam Long userId){
        try {
            return new ResponseEntity<>(userService.getUserInfo(userId), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "1차 최종 제출", description = "대표자의 연락처, 입금자명 저장 및 학교별 종목 정보 저장")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @PostMapping("/api/user/first-period-final-submit")
    public ResponseEntity<?> updateUserInfo(@RequestBody FinalSubmitRequestDto.FirstPeriod firstPeriodFinalSubmit){
        try {
            return new ResponseEntity<>(participantApplicationService.finalSubmitInFirstPeriod(firstPeriodFinalSubmit), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "1차 기간 최종 제출 확인(조회)")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/api/user/after-final-submit/first-period")
    public ResponseEntity<?> getFinalSubmitInfoInFirstPeriod(@RequestParam Long userId){
        try {
            return new ResponseEntity<>(participantApplicationService.getFinalSubmitInfoInFirstPeriod(userId), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "입금 정보 조회")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/api/user/final-submit/bank-info")
    public ResponseEntity<?> getDepositAccountInfo(){
        try {
            return new ResponseEntity<>(accountService.getDepositAccountInfo(), HttpStatus.OK);
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

    @Operation(summary = "대표자 정보 조회")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/api/user/after-final-submit/user-info")
    public ResponseEntity<?> getUserInfoWithDepositInfo(@RequestParam Long userId){
        try {
            return new ResponseEntity<>(userService.getUserInfo(userId), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
