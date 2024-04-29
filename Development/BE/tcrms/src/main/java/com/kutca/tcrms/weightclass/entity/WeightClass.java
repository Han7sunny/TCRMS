package com.kutca.tcrms.weightclass.entity;

import com.kutca.tcrms.participant.entity.Participant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeightClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weightClassId;

    private String name;

    @OneToMany(mappedBy = "weightClass")
    private List<Participant> participants = new ArrayList<>();
}
