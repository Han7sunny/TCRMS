package com.kutca.tcrms.file.repository;

import com.kutca.tcrms.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    int countAllByParticipant_ParticipantId(Long participant_id);
    List<File> findAllByParticipant_ParticipantId(Long participant_id);
}
