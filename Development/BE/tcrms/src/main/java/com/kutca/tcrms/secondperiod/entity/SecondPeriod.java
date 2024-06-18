package com.kutca.tcrms.secondperiod.entity;

import com.kutca.tcrms.account.entity.Account;
import com.kutca.tcrms.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecondPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long secondPeriodId;

    private Boolean isSecondApply;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", unique = true)
    private Account account;
}
