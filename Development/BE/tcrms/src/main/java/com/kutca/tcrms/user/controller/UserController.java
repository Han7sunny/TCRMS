package com.kutca.tcrms.user.controller;

import com.kutca.tcrms.user.controller.dto.request.LoginRequestDto;
import com.kutca.tcrms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto){
        return userService.login(loginRequestDto);
    }

}
