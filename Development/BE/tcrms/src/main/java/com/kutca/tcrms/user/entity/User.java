package com.kutca.tcrms.user.entity;

import com.kutca.tcrms.common.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String universityName;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role auth;

    private String phoneNumber;

    private String depositorName;

    @Column(columnDefinition = "boolean default true")
    private Boolean isFirstLogin;

    private Boolean isEditable;

    private Boolean isDepositConfirmed;

//    @Builder
//    public User(String universityName, String username, String password, Role role) {
//        this.universityName = universityName;
//        this.username = username;
//        this.password = password;
//        this.role = role;
//    }
}
