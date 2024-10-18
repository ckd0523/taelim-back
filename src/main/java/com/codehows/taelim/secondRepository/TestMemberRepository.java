package com.codehows.taelim.secondRepository;

import com.codehows.taelim.secondEntity.TestMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional("secondaryTransactionManager")
public interface TestMemberRepository extends JpaRepository<TestMember, String> {
    Optional<TestMember> findByEmail(String email);

    //쿼리 어노테이션으로 안하면 TestMember를 반환하려고 하기 때문에 String으로 반환할 시 오류 생김
    @Query("SELECT tm.uName FROM TestMember tm WHERE tm.email = :email")
    String findUNameByEmail(@Param("email") String email);
}