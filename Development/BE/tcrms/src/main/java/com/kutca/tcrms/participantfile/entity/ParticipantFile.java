package com.kutca.tcrms.participantfile.entity;

import com.kutca.tcrms.participant.entity.Participant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantFileId;

    @ColumnDefault("false")
    private Boolean isAllFileCompleted;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;
}