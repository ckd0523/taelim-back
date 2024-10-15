package com.codehows.taelim.security;

import com.codehows.taelim.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    //private final UserDetailsService userDetailsService;
    private final PasswordHasher passwordHasher;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordHasher2 passwordHasher2;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("커스텀 프로바이더 1");
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        System.out.println("커스텀 프로바이더 email : " + email);
        System.out.println("커스텀 프로바이더 password : " + password);

        UserDetails user = customUserDetailsService.loadUserByUsername(email);

        int[] iterCount = new int[1];  // 반복 횟수를 저장할 배열
        String prf = "PBKDF2WithHmacSHA256"; // 사용된 PRF

        System.out.println("커스텀 프로바이더 DB에 있는 해싱된 비밀번호 : " + user.getPassword());

        try {
            String hashedPassword = passwordHasher2.hashPasswordV3(password);
            System.out.println("커스텀 프로바이더 받은 비밀번호 해싱 결과 : " + hashedPassword);
            if(hashedPassword.equals(user.getPassword())){
                System.out.println("커스텀 프로바이더 비밀번호 비교 성공");
                return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        if (!passwordHasher2.verifyHashedPasswordV3(user.getPassword().getBytes(), password, iterCount, prf)) {
//            System.out.println("커스텀 프로바이더 실패");
//            return null;
//            //throw new BadCredentialsException("비밀번호 불일치");
//        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}