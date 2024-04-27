package com.kutca.tcrms.user.service;


import com.kutca.tcrms.common.security.JWTTokenProvider;
import com.kutca.tcrms.user.controller.dto.request.LoginRequestDto;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

//    private final UserRepository userRepository;
    private final JWTTokenProvider jwtTokenProvider;

    public String login(LoginRequestDto loginRequestDto) {
//        User user = userRepository.findByUniversityNameAndUsernameAndPassword(loginRequestDto.getUniversityName(), loginRequestDto.getUsername(), loginRequestDto.getPassword());
//        return jwtTokenProvider.createToken(user.getId(), user.getRole().toString());
        return jwtTokenProvider.createToken(1L, "USER");
    }
}
