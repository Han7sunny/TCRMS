package com.kutca.tcrms.universityapplication.entity;

import com.kutca.tcrms.event.entity.Event;
import com.kutca.tcrms.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UniversityApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long universityApplicationId;

    private int teamCount;

    private String period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
}
