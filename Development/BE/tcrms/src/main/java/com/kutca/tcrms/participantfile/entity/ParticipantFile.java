package com.kutca.tcrms.participantfile.entity;

import com.kutca.tcrms.participant.entity.Participant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@AllArgsConstructor
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

    public ParticipantFile updateIsAllFileCompleted(Boolean isAllFileCompleted){
        this.isAllFileCompleted = isAllFileCompleted;
        return this;
    }
}
