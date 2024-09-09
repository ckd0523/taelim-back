package com.codehows.taelim.entity;

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
public class Member {
    @Id
    @Column(name = "email")
    private String email;

    private String password;

    private String uName;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member(String uName){
        this.uName = uName;
    }


    public Member orElse(Member member) {

        return member;
    }
}

