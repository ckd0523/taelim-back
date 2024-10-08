package com.codehows.taelim.loginEntity;

import com.codehows.taelim.constant.Department;
import com.codehows.taelim.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Enumerated(EnumType.STRING)
    private Department department; //사용자의 부서

    @Enumerated(EnumType.STRING)
    private Role role; //일반사용자, 자산담당자, 관리자

    public TestMember toEntity(String email, String password, String uName, Department department, Role role) {
        this.email = email;
        this.password = password;
        this.uName = uName;
        this.department = department;
        this.role = role;
        return this;
    }
}
