package com.codehows.taelim.secondEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Base64;

@Entity
@Table(name = "aspnetusers")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AspNetUser {
    @Id
    @Column(name = "Id")
    private String id;

    @Column(name = "UserName", nullable = true)
    private String username; //계정명

    @Column(name = "PasswordHash", nullable = true)
    private String password; //hashing된 password

    @Column(name = "FullName")
    private String fullname; //실제 사용자 이름

    @Column(name = "Department", length = 20)
    private Long department; //사용자의 부서

    @Column(name = "EmailConfirmed")
    private boolean emailconfirmed;

    public AspNetUser toEntity(String username, String password, String fullname, Long department, boolean emailconfirmed) {
        this.username = Base64.getEncoder().encodeToString(username.getBytes());
        this.password = password;
        this.fullname = Base64.getEncoder().encodeToString(fullname.getBytes());
        //this.department = Base64.getEncoder().encodeToString(department.getBytes());
        //this.role = role;
        this.emailconfirmed = emailconfirmed;
        return this;
    }
}
