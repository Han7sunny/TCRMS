package com.kutca.tcrms.participantapplication.entity;

import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.participant.entity.Participant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantApplicationId;

    private int eventTeamNumber;

    private Boolean is2ndCancel;

    private Boolean is2ndChange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

}
