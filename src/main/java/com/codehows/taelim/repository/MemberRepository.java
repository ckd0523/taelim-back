package com.codehows.taelim.repository;

import com.codehows.taelim.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    //assetSurveyService에서 자산조사 등록을 할 때 사용하던거임
    //email을 통해 member를 가져올 때 실패하거나 하는 예외 상황이 발생하지 않을 것으로 예상되기 떄문에
    //2번째 findbyEmail 메서드를 사용하기로 함
    //Optional<Member> findByEmail(String email);

    //이때 매개 변수 assetUser는 email임
    Member findByEmail(String assetUser);
//    Member findByUName(String uName);
}
