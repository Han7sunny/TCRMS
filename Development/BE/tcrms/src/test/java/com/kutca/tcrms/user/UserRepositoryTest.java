package com.kutca.tcrms.user;

import com.kutca.tcrms.common.enums.Role;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void Boolean_타입_초기값() {

        //  given
        User user = User.builder()
                .universityName("서울대학교")
                .username("홍길동")
                .password("password")
                .auth(Role.USER)
                .build();

        //  when
        User savedUser = userRepository.save(user);

        //  then
        assertThat(savedUser.getIsFirstLogin()).isEqualTo(null);
        assertThat(savedUser.getIsEditable()).isEqualTo(null);
        assertThat(savedUser.getIsDepositConfirmed()).isEqualTo(null);
    }
}
