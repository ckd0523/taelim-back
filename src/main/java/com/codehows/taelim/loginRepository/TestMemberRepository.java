package com.codehows.taelim.loginRepository;

import com.codehows.taelim.loginEntity.TestMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestMemberRepository extends JpaRepository<TestMember, String> {
}