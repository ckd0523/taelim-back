package com.codehows.taelim.controller;

import com.codehows.taelim.dto.LoginRequest;
import com.codehows.taelim.dto.LoginResponse;
import com.codehows.taelim.security.JwtUtil;
import com.codehows.taelim.service.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("로그인 컨트롤러 1");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        if(!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login failed");
        }

        System.out.println("로그인 컨트롤러 2");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        // Create HttpOnly cookie for refresh token with SameSite attribute
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // for HTTPS
                .path("/") // path where the cookie will be used
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("Lax") // Add SameSite attribute
                .build();
        System.out.println("로그인 컨트롤러 3");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new LoginResponse(accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        System.out.println("리프레시 1");
        // 쿠키에서 리프레시 토큰 가져오기
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    System.out.println("리프레시 2");
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null) {
            // 리프레시 토큰이 존재하지 않는 경우
            System.out.println("리프레시 토큰이 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is missing");
        }


        System.out.println("리프레시 3");
        if (refreshToken != null && jwtUtil.isTokenExpired(refreshToken)) {
            // 리프레시 토큰이 유효하면 사용자 정보를 가져옴
            System.out.println("리프레시 4");
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // 새로운 액세스 토큰 발급
            String newAccessToken = jwtUtil.generateAccessToken(userDetails);
            System.out.println("리프레시 5");
            return ResponseEntity.ok(new LoginResponse(newAccessToken));
        } else {
            // 리프레시 토큰이 유효하지 않으면 401 응답
            System.out.println("리프레시 6");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is invalid");
        }
    }
}
