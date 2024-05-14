package com.kutca.tcrms.participantapplication.repository;

import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantApplicationRepository extends JpaRepository<ParticipantApplication, Long> {
    List<ParticipantApplication> findAllByParticipant_ParticipantId(Long participant_id);
}
