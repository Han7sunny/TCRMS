package com.kutca.tcrms.user;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.common.enums.Role;
import com.kutca.tcrms.common.security.JWTTokenProvider;
import com.kutca.tcrms.user.controller.dto.request.ChangePwRequestDto;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import com.kutca.tcrms.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    JWTTokenProvider jwtTokenProvider;


    @BeforeEach
    void setUp() {
        //  객체 주입
        userService = new UserService(jwtTokenProvider, userRepository);
    }

    @Test
    void changePasswordFail() {
        //  given
        User user = User.builder().username("홍길동").universityName("서울대학교").password("1234").auth(Role.USER).isFirstLogin(true).isEditable(true).isDepositConfirmed(false).build();
        ChangePwRequestDto changePwRequestDto = ChangePwRequestDto.builder()
                .userId(user.getUserId())
                .initPassword("5678")
                .build();

        //  when
        ResponseDto<?> responseDto = userService.changePassword(changePwRequestDto);

        //  then
        assertThat(responseDto.getIsSuccess()).isEqualTo(false);
    }

}