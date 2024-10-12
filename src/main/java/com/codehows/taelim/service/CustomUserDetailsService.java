package com.codehows.taelim.service;

import com.codehows.taelim.loginEntity.TestMember;
import com.codehows.taelim.loginRepository.TestMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TestMemberRepository testMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //데이터베이스에서 사용자 찾기
        TestMember member = testMemberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));

        // 사용자의 권한 설정
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(member.getRole().name())
        );

        // UserDetails 반환 (Spring Security에서 제공하는 User 객체 사용)
        return new User(
                member.getEmail(),
                member.getPassword(),
                authorities // 사용자 권한 설정
                //Collections.emptyList()
        );
    }
}
