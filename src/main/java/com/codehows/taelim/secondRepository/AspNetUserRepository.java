package com.codehows.taelim.secondRepository;


import com.codehows.taelim.secondEntity.AspNetUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AspNetUserRepository extends JpaRepository<AspNetUser, String> {
    Optional<AspNetUser> findByUsername(String email);

    //자산 조회에서 사용자를 위한 검색 조건에 필요한거
    List<AspNetUser> findByFullnameContaining(String fullname);

    
    

    // fullname을 BASE64로 인코딩한 값과 부분적으로 일치하는 사용자 검색
    @Query("SELECT u FROM AspNetUser u WHERE u.fullname LIKE %:encodedFullname%")
    List<AspNetUser> findByEncodedFullname(@Param("encodedFullname") String encodedFullname);

    //Optional<AspNetUser> findById(String id);

//쿼리 어노테이션으로 안하면 AspNetUser를 반환하려고 하기 때문에 String으로 반환할 시 오류 생김
    @Query("SELECT user FROM AspNetUser user WHERE user.username = :email")
    AspNetUser findFullNameByEmail(String email);
}
