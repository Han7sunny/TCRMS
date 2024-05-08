package com.kutca.tcrms.user.controller;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.user.controller.dto.request.ChangePwRequestDto;
import com.kutca.tcrms.user.controller.dto.request.LoginRequestDto;
import com.kutca.tcrms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
