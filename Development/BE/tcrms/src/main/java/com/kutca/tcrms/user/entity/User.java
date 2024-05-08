package com.kutca.tcrms.user.entity;

import com.kutca.tcrms.common.enums.Role;
import com.kutca.tcrms.participant.entity.Participant;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "`user`")
@DynamicInsert
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String universityName;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role auth;

    @Nullable
    private String phoneNumber;

    @Nullable
    private String depositorName;

//    @Column(name = "isFirstLogin", columnDefinition = "boolean default true")
    @ColumnDefault("true")
    private Boolean isFirstLogin;

    @ColumnDefault("false")
    private Boolean isEditable;

    @ColumnDefault("false")
    private Boolean isDepositConfirmed;

    @OneToMany(mappedBy = "user")
    private List<Participant> participants = new ArrayList<>();

    public User changePassword(String newPassword) {
        this.password = newPassword;
        this.isFirstLogin = false;
        return this;
    }
}
