package com.kutca.tcrms.user.entity;

import com.kutca.tcrms.common.enums.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

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
    private Long id;

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

//    @Builder
//    public User(String universityName, String username, String password, Role role) {
//        this.universityName = universityName;
//        this.username = username;
//        this.password = password;
//        this.role = role;
//    }
}
