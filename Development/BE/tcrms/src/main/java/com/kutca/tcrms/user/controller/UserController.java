package com.kutca.tcrms.user.controller;

import com.kutca.tcrms.user.controller.dto.request.LoginRequestDto;
import com.kutca.tcrms.user.controller.dto.response.LoginResponseDto;
import com.kutca.tcrms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
            LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
            if (!loginResponseDto.getIsSuccess()) {
                System.out.println("로그인 실패");
            }
            return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>("예외발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
