package com.codehows.taelim.secondEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;
import java.util.List;

@Entity
@Table(name = "aspnetusers")
@NoArgsConstructor
@Getter
@Setter
public class AspNetUser {
    @Id
    @Column(name = "Id")
    private String id;

    private String email; //계정명

    private String password; //hashing된 password

    private String username; //실제 사용자 이름

    private String department; //사용자의 부서

    //private String role; //일반사용자, 자산담당자, 관리자

    private boolean emailconfirmed;

    @OneToMany(mappedBy = "user")
    private List<AspNetUserRole> userRoles; // AspNetUserRoles와의 관계

    public AspNetUser toEntity(String email, String password, String username, String department, boolean emailconfirmed) {
        this.email = Base64.getEncoder().encodeToString(email.getBytes());
        this.password = password;
        this.username = Base64.getEncoder().encodeToString(username.getBytes());
        this.department = Base64.getEncoder().encodeToString(department.getBytes());
        //this.role = role;
        this.emailconfirmed = emailconfirmed;
        return this;
    }
}
