package com.kutca.tcrms.file.entity;

import com.kutca.tcrms.participantfile.entity.ParticipantFile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@AllArgsConstructor
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

    public File updateFilePath(String filePath){
        this.filePath = filePath;
        return this;
    }
}
