package com.kutca.tcrms.user.service;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.common.security.JWTTokenProvider;
import com.kutca.tcrms.user.controller.dto.request.ChangePwRequestDto;
import com.kutca.tcrms.user.controller.dto.request.LoginRequestDto;
import com.kutca.tcrms.user.controller.dto.request.UserRequestDto;
import com.kutca.tcrms.user.controller.dto.response.LoginResponseDto;
import com.kutca.tcrms.user.controller.dto.response.UserResponseDto;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ResponseDto<?> changePassword(ChangePwRequestDto changePwRequestDto) {

        Optional<User> findUser = userRepository.findByUserIdAndPassword(changePwRequestDto.getUserId(), changePwRequestDto.getInitPassword());
        if(findUser.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("초기 비밀번호가 일치하지 않습니다.")
                    .build();
        }

        userRepository.save(findUser.get().changePassword(changePwRequestDto.getNewPassword()));
        return ResponseDto.builder()
                .isSuccess(true)
                .message("비밀번호가 변경되었습니다.")
                .build();
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getUserInfo(Long userId){

        Optional<User> findUser = userRepository.findById(userId);
        if(findUser.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("대표자 정보를 조회할 수 없습니다.")
                    .build();
        }

        User user = findUser.get();
        return ResponseDto.builder()
                .isSuccess(true)
                .payload(
                        UserResponseDto.builder()
                                .userName(user.getUsername())
                                .phoneNumber(user.getPhoneNumber())
                                .universityName(user.getUniversityName())
                                .depositorName(user.getDepositorName())
                )
                .build();
    }

    @Transactional
    public ResponseDto<?> updateUserInfo(UserRequestDto.Info userInfoRequestDto){

        Optional<User> findUser = userRepository.findById(userInfoRequestDto.getUserId());
        if(findUser.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("대표자 정보를 조회할 수 없습니다.")
                    .build();
        }

        User user = findUser.get();
        user.updatePhoneNumberAndDepositorName(userInfoRequestDto.getPhoneNumber(), userInfoRequestDto.getDepositorName());
        userRepository.save(user);

        return ResponseDto.builder()
                .isSuccess(true)
                .build();

    }
}