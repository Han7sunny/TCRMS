package com.kutca.tcrms.participant.entity;

import com.kutca.tcrms.participantfile.entity.ParticipantFile;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.weightclass.entity.WeightClass;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    private String name;

    @Nullable
    private String identityNumber;

    private String gender;

    private String universityName;

    private Boolean isForeigner;

    @Nullable
    private String nationality;

    @Nullable
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weight_class_id")
    private WeightClass weightClass;

    @OneToOne(mappedBy = "participant")
    private ParticipantFile participantFile;
}
