package com.kutca.tcrms.user.controller;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.user.controller.dto.request.ChangePwRequestDto;
import com.kutca.tcrms.user.controller.dto.request.LoginRequestDto;
import com.kutca.tcrms.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "UserController")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "로그인")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto){
        try {
            ResponseDto<?> loginResponseDto = userService.login(loginRequestDto);
            return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("예외발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "초기 비밀번호 변경", description = "isFirstLogin이 true일 경우에만 비밀번호 변경")
    @PostMapping("/api/changePW")
    public ResponseEntity<?> changePassword(@RequestBody ChangePwRequestDto changePwRequestDto) {
        try {
            ResponseDto<?> changePasswordDto = userService.changePassword(changePwRequestDto);
            return new ResponseEntity<>(changePasswordDto, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("예외발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
