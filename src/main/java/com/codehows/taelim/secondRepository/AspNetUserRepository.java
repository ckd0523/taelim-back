package com.codehows.taelim.secondRepository;


import com.codehows.taelim.secondEntity.AspNetUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AspNetUserRepository extends JpaRepository<AspNetUser, Long> {
    Optional<AspNetUser> findByEmail(String email);
}
