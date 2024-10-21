package com.codehows.taelim.secondEntity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class AspNetUserRoleId implements Serializable {
    private String userId;
    private String roleId;
}
