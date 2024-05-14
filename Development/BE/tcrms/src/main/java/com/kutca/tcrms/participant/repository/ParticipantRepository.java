package com.kutca.tcrms.participant.repository;

import com.kutca.tcrms.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findAllByUser_UserId(Long user_id);
}
