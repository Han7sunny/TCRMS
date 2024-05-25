package com.kutca.tcrms.participant.entity;

import com.kutca.tcrms.participant.controller.dto.request.IndividualParticipantRequestDto;
import com.kutca.tcrms.participantfile.entity.ParticipantFile;
import com.kutca.tcrms.user.entity.User;
import com.kutca.tcrms.weightclass.entity.WeightClass;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
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

    public Participant updateWeightClass(WeightClass weightClassId) {
        this.weightClass = weightClassId;
        return this;
    }
    public Participant updateParticipant(IndividualParticipantRequestDto.Modify participant) {
        this.name = participant.getName();
        this.identityNumber = participant.getIdentityNumber();
        this.gender = participant.getGender();
        this.isForeigner = participant.getIsForeigner();
        this.nationality = participant.getNationality();
        this.phoneNumber = participant.getPhoneNumber();
        return this;
    }
}
