package com.kutca.tcrms.file.entity;

import com.kutca.tcrms.participantfile.entity.ParticipantFile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String fileName;

    private String filePath;

    @ColumnDefault("false")
    private Boolean isFileConfirmed;

    private Boolean isFileUncorrect;

    @ManyToOne
    @JoinColumn(name = "participant_file_id")
    private ParticipantFile participantFile;
}
