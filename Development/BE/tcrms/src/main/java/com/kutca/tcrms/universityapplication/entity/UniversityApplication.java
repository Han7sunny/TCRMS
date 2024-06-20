package com.kutca.tcrms.universityapplication.entity;

import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UniversityApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long universityApplicationId;

    private String eventName;

    private int teamCount;

    private int teamFee;

    private String period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
