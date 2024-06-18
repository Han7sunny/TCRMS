package com.kutca.tcrms.universityapplication.repository;

import com.kutca.tcrms.universityapplication.entity.UniversityApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityApplicationRepository extends JpaRepository<UniversityApplication, Long> {
    Boolean existsByUser_UserId(Long user_id);
}
