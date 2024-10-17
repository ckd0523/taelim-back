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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
//JwtAuthenticationFilter는 "모든 요청에 대해" JWT 토큰을 검사, OncePerRequestFilter를 상속하여 요청 당 한 번씩만 실행되록함
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    //private final UserDetailsService userDetailsService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("커스텀 필터1");

        // 헤더에서 JWT를 추출
        // 이 때 Authorization 값을 읽는데 이는 보통 Bearer [토큰값]임
        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        //if 문을 통해 실제 JWT 토큰을 추출
        // 헤더가 존재하고, "Bearer "로 시작하는 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

//            if(jwtUtil.isTokenExpired(jwt)) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token is invalid");
//            }

            try {
                // JWT에서 사용자 이름을 추출
                email = jwtUtil.getUsernameFromToken(jwt);
                // 사용자 이름을 못 찾았을 때 처리
            } catch (ExpiredJwtException e) {
                //액세스 토큰이 만료된 경우
                logger.error("JWT token has expired", e);
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token is invalid");
            } catch (JwtException e) {
                //다른 이유로 예외 발생
                logger.error("JWT token is invalid", e);
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token is missing");
            }
        }
        System.out.println("커스텀 필터2");
        // 사용자가 인증되지 않은 경우 (SecurityContext에 인증 정보가 없는 경우)
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("커스텀 필터 이메일 : " + email);

            //username에 해당하는 사용자 정보를 로드. 보통 데이터베이스에서 사용자의 정보 및 권한을 가져오는 역할을 합니다.
            UserDetails user = customUserDetailsService.loadUserByUsername(email);

            //인증된 사용자에 대해 토큰이 유효한지 검사
            //사용자 정보에 있는 jwt를 기반으로 jwt 토큰을 생성하는데 내가 페이지에 처음 들어온 사용자라면
            //jwt가 null일 텐데 그러면 if문을 수행하지 않고 chain을 통과하여 컨트롤러로 전달되게 됨.
            //이후 컨트롤러에서 요청을 처리
            System.out.println("커스텀 필터3");
            if (jwtUtil.validateToken(jwt, user)) {
                // 사용자 정보를 기반으로 인증 토큰 생성
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token is invalid");
            }
        }
        System.out.println("커스텀 필터4");
        // 사용자가 인증되었다면 인증 토큰을 발급하지 않고 요청에 따른 컨트롤러 수행
        filterChain.doFilter(request, response);
    }
}