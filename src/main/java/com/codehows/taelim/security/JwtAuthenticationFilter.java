package com.codehows.taelim.security;

import com.codehows.taelim.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

/**
 * JWT 인증을 처리하는 필터 클래스
 * 모든 HTTP 요청에 대해 JWT 토큰을 검증하고 인증을 처리
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = 7;

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // JWT 토큰 추출 및 처리
            String token = extractJwtToken(request);
            if (token != null) {
                processToken(token, request);
            }
        } catch (Exception e) {
            handleAuthenticationException(e, response);
            return;
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청 헤더에서 JWT 토큰을 추출
     * @param request HTTP 요청
     * @return 추출된 JWT 토큰 또는 null
     */
    private String extractJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX_LENGTH);
        }
        return null;
    }

    /**
     * JWT 토큰을 처리하고 인증 정보를 설정
     * @param token JWT 토큰
     * @param request HTTP 요청
     */
    private void processToken(String token, HttpServletRequest request) {
        // getUsernameFromToken 메서드 내에서 이미 토큰 유효성 검증이 수행됨
        String email = jwtUtil.getUsernameFromToken(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            authenticateUser(email, request);
        }
    }

    /**
     * 사용자 인증 처리 및 SecurityContext 설정
     * @param email 사용자 이메일
     * @param request HTTP 요청
     */
    private void authenticateUser(String email, HttpServletRequest request) {
        // Base64로 인코딩된 이메일을 사용하여 사용자 정보 로드
        UserDetails user = customUserDetailsService.loadUserByUsername(
                email);

        // 토큰이 유효하다고 확인되었으므로 바로 인증 처리
        UsernamePasswordAuthenticationToken authToken = createAuthenticationToken(user, request);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    /**
     * 인증 토큰 생성
     * @param user 사용자 정보
     * @param request HTTP 요청
     * @return 생성된 인증 토큰
     */
    private UsernamePasswordAuthenticationToken createAuthenticationToken(UserDetails user,
                                                                          HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authToken;
    }

    /**
     * 인증 예외 처리
     * @param e 발생한 예외
     * @param response HTTP 응답
     */
    private void handleAuthenticationException(Exception e, HttpServletResponse response)
            throws IOException {
        SecurityContextHolder.clearContext();

        if (e instanceof ExpiredJwtException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token has expired");
        } else if (e instanceof JwtException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication failed");
        }
    }
}