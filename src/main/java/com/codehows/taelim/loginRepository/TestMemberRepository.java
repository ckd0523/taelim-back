package com.codehows.taelim.loginRepository;

import com.codehows.taelim.loginEntity.TestMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional("secondaryTransactionManager")
public interface TestMemberRepository extends JpaRepository<TestMember, String> {
    Optional<TestMember> findByEmail(String email);
}