package com.codehows.taelim.secondRepository;

import com.codehows.taelim.secondEntity.AspNetUser;
import com.codehows.taelim.secondEntity.AspNetUserRole;
import com.codehows.taelim.secondEntity.AspNetUserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AspNetUserRoleRepository extends JpaRepository<AspNetUserRole, AspNetUserRoleId> {

    List<AspNetUserRole> findAllByUser(AspNetUser user);
}
