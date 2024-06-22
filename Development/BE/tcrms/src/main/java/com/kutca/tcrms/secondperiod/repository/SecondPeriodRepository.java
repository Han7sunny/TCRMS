package com.kutca.tcrms.secondperiod.repository;

import com.kutca.tcrms.secondperiod.entity.SecondPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecondPeriodRepository extends JpaRepository<SecondPeriod, Long> {
    Optional<SecondPeriod> findByUser_UserId(Long user_id);
}
