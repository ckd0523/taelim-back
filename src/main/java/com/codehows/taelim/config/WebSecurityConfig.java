package com.codehows.taelim.config;

import com.codehows.taelim.security.CustomAuthenticationProvider;
import com.codehows.taelim.security.JwtAuthenticationFilter;
import com.codehows.taelim.security.JwtUtil;
import com.codehows.taelim.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;  // JwtService 의존성 주입
    //private final UserDetailsService userDetailsService;
    private final CustomUserDetailsService customUserDetailsService;
    //밑에서 얘를 아무도 안쓰는데 주입해주는 이유는
    //AuthenticationManager는 기본적으로 여러 개의 AuthenticationProvider를 관리하는데
    //여러 개를 차례로 호출하여 인증을 시도 커스텀 provider를 manager에 적용하기 위해 주입
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 보호 비활성화
                //.cors(AbstractHttpConfigurer::disable)  // CORS 비활성화
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("/login", "/assetSurveyHistory").permitAll();  // 로그인 경로는 인증 필요 없음
                    requests.requestMatchers(HttpMethod.POST).authenticated();
                    requests.requestMatchers(HttpMethod.GET).authenticated();
                    requests.requestMatchers(HttpMethod.PUT).authenticated();
                    requests.requestMatchers(HttpMethod.DELETE).authenticated();
                })
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 세션을 사용하지 않음 (JWT)
                )
                //UsernamePasswordAuthentication 필터는 구현 안했는데 어떤 동작을 하는거지
                //이름만 본다면 유저 이름과 패스워드를 인증하는데 컨트롤러에서 하면 안되나?
                //일단 월요일에 저거 지워보자
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        return httpSecurity.build();
    }

    //authenticationManager가 자격 증명 인증을 하는 방법은
    //UserDetailsService와 PasswordEncoder를 기반으로 자격 증명을 검증
    //사용자가 이메일, 패스워드를 주면 UsernamePasswordAuthenticationToken 객체를 생성해
    //AuthenticationManager에 전달, 다시 AuthenticationProvider를 호출하여 사용자를 인증
    //AuthenticationProvider는 UserDetailsService를 사용해서
    //DB에 사용자의 정보를 로드하고 패스워드를 비교함
    //인증에 성공하면 Authentication 객체를 반환하고 실패하면 AuthenticationException Throw
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // CustomAuthenticationProvider를 사용하는 AuthenticationManager 등록
        return new ProviderManager(customAuthenticationProvider);
    }

    //얘는 왜 빈 주입을 하지? 필터를 사용하려고 하는건 뭔가 알겠는데
    //생성자에 jwtUtil과 userDetailService는 왜 주는거지
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, customUserDetailsService);
    }
}
