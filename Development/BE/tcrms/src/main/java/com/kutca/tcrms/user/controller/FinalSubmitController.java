package com.kutca.tcrms.user.controller;

import com.kutca.tcrms.account.service.AccountService;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.participantapplication.service.ParticipantApplicationService;
import com.kutca.tcrms.universityapplication.service.UniversityApplicationService;
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
    public ResponseEntity<?> getUserInfo(@RequestParam Long userId){
        try {
            return new ResponseEntity<>(userService.getUserInfo(userId), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "대표자 정보 갱신", description = "대표자의 연락처, 입금자명 저장")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @PostMapping("/api/user/after-final-submit/user-info")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserRequestDto.Info userInfoRequestDto){
        try {
            return new ResponseEntity<>(userService.updateUserInfo(userInfoRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
