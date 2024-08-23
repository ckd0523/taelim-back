package com.codehows.taelim.entity;

import com.codehows.taelim.constant.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
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
}
