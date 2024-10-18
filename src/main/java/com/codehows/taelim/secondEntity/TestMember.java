package com.codehows.taelim.secondEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;

@Entity
@Table(name = "member")
@NoArgsConstructor
@Getter
@Setter
public class TestMember {
    @Id
    @Column(name = "email")
    private String email; //계정명

    private String password; //hashing된 password

    private String uName; //실제 사용자 이름

    //@Enumerated(EnumType.STRING)
    private String department; //사용자의 부서

    //@Enumerated(EnumType.STRING)
    private String role; //일반사용자, 자산담당자, 관리자

    private boolean emailConfirmed;

    public TestMember toEntity(String email, String password, String uName, String department, String role, boolean emailConfirmed) {
        this.email = Base64.getEncoder().encodeToString(email.getBytes());
        this.password = password;
        this.uName = Base64.getEncoder().encodeToString(uName.getBytes());
        this.department = Base64.getEncoder().encodeToString(department.getBytes());
        this.role = role;
        this.emailConfirmed = emailConfirmed;
        return this;
    }
}
