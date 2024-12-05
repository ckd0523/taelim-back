package com.codehows.taelim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "emailSet")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailSet {
    @Id
    @Column(name = "emailSetNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailSetNo;
    private String setName;
    private String setEmail;
    private Boolean isSelected;
}
