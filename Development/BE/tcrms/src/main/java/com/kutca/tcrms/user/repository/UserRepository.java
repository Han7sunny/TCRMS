package com.kutca.tcrms.user.repository;

import com.kutca.tcrms.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUniversityNameAndUsernameAndPassword(String university_name, String username, String password);
    Optional<User> findByUserIdAndPassword(Long user_id, String password);
}
