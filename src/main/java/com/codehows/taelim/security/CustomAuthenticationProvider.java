package com.codehows.taelim.security;

import com.codehows.taelim.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordHasher passwordHasher;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("커스텀 프로바이더 1");
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        System.out.println("커스텀 프로바이더 email : " + email);
        System.out.println("커스텀 프로바이더 password : " + password);

        UserDetails user = customUserDetailsService.loadUserByUsername(email);
        //UserDetails user = customUserDetailsService.loadUserByUsername(Base64.getEncoder().encodeToString(email.getBytes()));
        System.out.println("커스텀 프로바이더 인가?" + user.getAuthorities());

        System.out.println("커스텀 프로바이더 DB에 있는 해싱된 비밀번호 : " + user.getPassword());

        try {
            //String hashedPassword = passwordHasher.hashPasswordV3(password);
            //System.out.println("커스텀 프로바이더 받은 비밀번호 해싱 결과 : " + hashedPassword);

            //boolean result = hashedPassword.equals(user.getPassword());
            boolean result = passwordHasher.verifyHashedPasswordV3(user.getPassword(), password);

            if(result) {
                System.out.println("커스텀 프로바이더 비밀번호 비교 성공");
                return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

//        if (!passwordHasher2.verifyHashedPasswordV3(user.getPassword().getBytes(), password, iterCount, prf)) {
//            System.out.println("커스텀 프로바이더 실패");
//            return null;
//            //throw new BadCredentialsException("비밀번호 불일치");
//        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}