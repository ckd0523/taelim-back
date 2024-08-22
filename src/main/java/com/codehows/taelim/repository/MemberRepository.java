package com.codehows.taelim.repository;

import com.codehows.taelim.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
