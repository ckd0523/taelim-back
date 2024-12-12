package com.codehows.taelim.secondEntity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "aspnetroles")
@NoArgsConstructor
@Getter
@Setter
public class AspNetRole {
    @Id
    @Column(name = "Id")
    private String id;

    @Column(name = "Name")
    private String name;
}
