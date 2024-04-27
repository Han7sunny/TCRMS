package com.kutca.tcrms.user.repository;

import com.kutca.tcrms.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUniversityNameAndUsernameAndPassword(String university_name, String username, String password);
}
