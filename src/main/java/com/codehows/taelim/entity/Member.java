package com.codehows.taelim.entity;

import com.codehows.taelim.constant.Role;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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


}
