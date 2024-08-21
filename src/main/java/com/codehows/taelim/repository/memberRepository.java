package com.codehows.taelim.repository;

import com.codehows.taelim.entity.member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface memberRepository extends JpaRepository<member, Long> {
}
