package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="userLog")
public class UserLogEntity implements Serializable
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userID")
    private UserEntity userEntity;

    @NotNull
    @Column(name="lastLogin")
    private String lastLogin;

    @NotNull
    @Column(name="lastLogout")
    private String lastLogout;

    @NotNull
    @Column(name="sessionDuration")
    private int sessionDuration;

    @Column(name="loginSuccess")
    private boolean loginSuccess;

    @Column(name="loginAttempts")
    private int loginAttempts;

    @Column(name="isActive")
    private boolean isActive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLogEntity that = (UserLogEntity) o;
        return id == that.id && sessionDuration == that.sessionDuration && loginSuccess == that.loginSuccess && loginAttempts == that.loginAttempts && isActive == that.isActive && Objects.equals(userEntity, that.userEntity) && Objects.equals(lastLogin, that.lastLogin) && Objects.equals(lastLogout, that.lastLogout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userEntity, lastLogin, lastLogout, sessionDuration, loginSuccess, loginAttempts, isActive);
    }
}
