package com.codehows.taelim.secondRepository;

import com.codehows.taelim.secondEntity.AspNetRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AspNetRoleRepository extends JpaRepository<AspNetRole, Long> {
   List<AspNetRole> findNameById(String id);
}
