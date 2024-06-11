package com.kutca.tcrms.participantfile.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantFileRepository extends JpaRepository<ParticipantFile, Long> {
    Optional<ParticipantFile> findByParticipant_ParticipantId(Long participant_id);
}
