package com.codehows.taelim.service;

import com.codehows.taelim.secondEntity.AspNetUser;
import com.codehows.taelim.secondEntity.TestMember;
import com.codehows.taelim.secondRepository.AspNetUserRepository;
import com.codehows.taelim.secondRepository.TestMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TestMemberRepository testMemberRepository;
    private final AspNetUserRepository aspNetUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        

        AspNetUser aspNetUser = aspNetUserRepository.findByUsername(email).orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        //유저 정보를 받아올 때 이메일 승인 검증
        if(!aspNetUser.isEmailconfirmed()) {
            System.out.println("이메일 미승인");
            return new User(aspNetUser.getUsername(), "", Collections.emptyList());
        }
        
        List<String> roles = aspNetUser.getUserRoles()
                .stream().map(userRole -> userRole.getRole().getName())
                .toList();

        List<GrantedAuthority> authorities;

        if(roles.contains("ADMIN")) {
            authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if(roles.contains("ASSET_MANAGER")) {
            authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_ASSET_MANAGER"));
        } else {
            authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_USER"));
        }

        //시스템관리자
//        if(roles.contains("SystemAdmin")) {
//            authorities = Collections.singletonList(
//                    new SimpleGrantedAuthority("ROLE_ADMIN"));
//            //자산관리 담당자, 책임자
//        } else if(roles.contains("AssetAdmin") || roles.contains("AssetManager")) {
//            authorities = Collections.singletonList(
//                    new SimpleGrantedAuthority("ROLE_ASSET_MANAGER"));
//            //나머지는 User 권한
//        } else {
//            authorities = Collections.singletonList(
//                    new SimpleGrantedAuthority("ROLE_USER"));
//        }

        // UserDetails 반환 (Spring Security에서 제공하는 User 객체 사용)
        return new User(
                new String(Base64.getDecoder().decode(aspNetUser.getUsername())),
                aspNetUser.getPassword(),
                authorities // 사용자 권한 설정
        );
    }
}
