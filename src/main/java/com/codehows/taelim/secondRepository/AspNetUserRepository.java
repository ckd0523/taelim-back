package com.codehows.taelim.secondRepository;


import com.codehows.taelim.secondEntity.AspNetUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AspNetUserRepository extends JpaRepository<AspNetUser, Long> {
    Optional<AspNetUser> findByUsername(String email);

    //쿼리 어노테이션으로 안하면 AspNetUser를 반환하려고 하기 때문에 String으로 반환할 시 오류 생김
    @Query("SELECT user.fullname FROM AspNetUser user WHERE user.username = :email")
    String findFullNameByEmail(String email);
}
