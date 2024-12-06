package com.codehows.taelim.secondEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.mapping.ToOne;

@Entity
@Table(name = "aspnetuserroles")
@NoArgsConstructor
@Getter
@Setter
public class AspNetUserRole {
    @EmbeddedId
    private AspNetUserRoleId id; // 복합키

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "UserId")
    private AspNetUser user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "RoleId")
    private AspNetRole role;

}
