package com.kutca.tcrms.user.service;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.common.security.JWTTokenProvider;
import com.kutca.tcrms.user.controller.dto.request.LoginRequestDto;
import com.kutca.tcrms.user.controller.dto.response.LoginResponseDto;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JWTTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public ResponseDto<?> login(LoginRequestDto loginRequestDto) {
        Optional<User> findUser = userRepository.findByUniversityNameAndUsernameAndPassword(loginRequestDto.getUniversityName(), loginRequestDto.getUsername(), loginRequestDto.getPassword());
        if(findUser.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("회원정보를 찾을 수 없습니다.")
                    .build();
        }

        User user = findUser.get();
        return ResponseDto.builder()
                .isSuccess(true)
                .payload(
                        LoginResponseDto.builder()
                                .userId(user.getUserId())
                                .isFirstLogin(user.getIsFirstLogin())
                                .token(jwtTokenProvider.createToken(user.getUserId(), user.getAuth().name()))
                                .auth(user.getAuth().name())
                        .build())
                .build();
    }
}
