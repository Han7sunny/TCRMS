package com.kutca.tcrms.universityapplication.repository;

import com.kutca.tcrms.universityapplication.entity.UniversityApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UniversityApplicationRepository extends JpaRepository<UniversityApplication, Long> {
    Boolean existsByUser_UserId(Long user_id);
    List<UniversityApplication> findAllByUser_UserIdAndPeriod(Long user_id, String period);
    Optional<UniversityApplication> findByUser_UserIdAndEventNameAndPeriod(Long user_id, String event_name, String period);
}
