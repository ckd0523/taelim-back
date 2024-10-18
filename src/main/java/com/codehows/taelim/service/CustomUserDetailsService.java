package com.codehows.taelim.service;

import com.codehows.taelim.secondEntity.TestMember;
import com.codehows.taelim.secondRepository.TestMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TestMemberRepository testMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //데이터베이스에서 사용자 찾기
        System.out.println("유저 디테일 서비스1 : " + email);
        TestMember member = testMemberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));

        System.out.println("유저 디테일 서비스2");
        System.out.println("유저 디테일 서비스3 : " + member.getRole());
        // 사용자의 권한 설정
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(member.getRole())
        );

        System.out.println("유저 디테일 서비스4 : " + new String(Base64.getDecoder().decode(member.getEmail())));

        // UserDetails 반환 (Spring Security에서 제공하는 User 객체 사용)
        return new User(
                new String(Base64.getDecoder().decode(member.getEmail())),
                member.getPassword(),
                authorities // 사용자 권한 설정
                //Collections.emptyList()
        );
    }
}
