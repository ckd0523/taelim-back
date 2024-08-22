package com.codehows.taelim.entity;

import com.codehows.taelim.constant.BackUpScope;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "backUpHistory")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BackUpHistory {

    @Id
    @Column(name = "backUpNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long backUpNo;

    private LocalDate backUpDate;
    private BackUpScope backUpScope;
}
