package com.codehows.taelim.service;

import com.codehows.taelim.secondEntity.AspNetRole;
import com.codehows.taelim.secondEntity.AspNetUser;
//import com.codehows.taelim.secondEntity.TestMember;
import com.codehows.taelim.secondEntity.AspNetUserRole;
import com.codehows.taelim.secondRepository.AspNetRoleRepository;
import com.codehows.taelim.secondRepository.AspNetUserRepository;
//import com.codehows.taelim.secondRepository.TestMemberRepository;
import com.codehows.taelim.secondRepository.AspNetUserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    private final AspNetUserRepository aspNetUserRepository;
    private final AspNetUserRoleRepository aspNetUserRoleRepository;
    @Value("${admin1}")
    private String admin1;

    @Value("${admin2}")
    private String admin2;

    @Value("${assetManager1}")
    private String assetManager1;

    @Value("${assetManager2}")
    private String assetManager2;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        

        AspNetUser aspNetUser = aspNetUserRepository.findByUsername(email).orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        //유저 정보를 받아올 때 이메일 승인 검증
        if(!aspNetUser.isEmailconfirmed()) {
            System.out.println("이메일 미승인");
            return new User(aspNetUser.getUsername(), "", Collections.emptyList());
        }

        List<String> roles = aspNetUserRoleRepository.findAllByUser(aspNetUser)
                .stream().map(AspNetUserRole::getRole)
                .map(AspNetRole::getName).toList();


        List<GrantedAuthority> authorities;

        //시스템관리자
        if(roles.contains(admin1) || roles.contains(admin2)) {
            authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_ADMIN"));
            //자산관리 담당자, 책임자
        } else if(roles.contains(assetManager1) || roles.contains(assetManager2)) {
            authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_ASSET_MANAGER"));
            //나머지는 User 권한
        } else {
            authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_USER"));
        }
        System.out.println("커스텀유저 : " + authorities.toString());

        // UserDetails 반환 (Spring Security에서 제공하는 User 객체 사용)
        return new User(
                aspNetUser.getUsername(),
                aspNetUser.getPassword(),
                authorities // 사용자 권한 설정
        );
    }
}
