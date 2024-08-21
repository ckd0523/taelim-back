package com.codehows.taelim.entity;

import com.codehows.taelim.constant.role;
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
public class member {
    @Id
    @Column(name = "email")
    private String email;

    private String password;
    private String uName;
    private role role;
}
