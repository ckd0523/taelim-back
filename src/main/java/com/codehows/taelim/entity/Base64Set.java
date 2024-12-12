package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "base64Set")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Base64Set {
    @Id
    @Column(name = "base64SetNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long base64SetNo;
    private boolean isEnabled;
}
