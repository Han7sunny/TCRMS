package com.kutca.tcrms.file.repository;

import com.kutca.tcrms.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    int countAllByParticipantFile_ParticipantFileId(Long participant_file_id);
    List<File> findAllByParticipantFile_ParticipantFileId(Long participant_file_id);
}
