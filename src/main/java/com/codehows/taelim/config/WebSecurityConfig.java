package com.codehows.taelim.config;

import com.codehows.taelim.security.CustomAuthenticationProvider;
import com.codehows.taelim.security.JwtAuthenticationFilter;
import com.codehows.taelim.security.JwtUtil;
import com.codehows.taelim.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
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
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    //LoginController
                    requests.requestMatchers("/login", "/refresh", "/logout").permitAll() // 로그인 경로는 인증 필요 없음
                            //각 요청에 대한 인가 설정
                            //Constant와 UserDetail에 role이 ROLE_로 시작해야 시큐리티가 인식함
                            //AssetController
                            .requestMatchers("/asset/register").hasAnyRole("ADMIN", "ASSET_MANAGER")
                            .requestMatchers("/asset/update/").hasRole("ADMIN")
                            .requestMatchers("/asset/updateDemand/").hasRole("ASSET_MANAGER")
                            .requestMatchers("/asset/excelRegister").hasAnyRole("ADMIN", "ASSET_MANAGER")
                            //DemandController
                            .requestMatchers("/DemandHistory").hasRole("ADMIN")
                            .requestMatchers("/DemandList").hasRole("ADMIN")
                            .requestMatchers("/updateAction").hasRole("ADMIN")
                            .requestMatchers("/deleteAction").hasRole("ADMIN")
                            .requestMatchers("/demandAction").hasRole("ADMIN")
                            //FileController and File 등록 요청은 등록 요청이 완료 되어야 요청이 오기 때문에 인가 설정 제외\
                            .requestMatchers("/file/**").permitAll()
                            //MaintainController
                            .requestMatchers("/maintain/**").hasAnyRole("ADMIN","ASSET_MANAGER")
                            .requestMatchers("/maintain/img/**").permitAll()
                            //QRController, 여기 잘 봐야함
                            .requestMatchers("/disposeAsset/").hasRole("ADMIN")
                            .requestMatchers("/disposeDemand/").hasRole("ASSET_MANAGER")
                            .requestMatchers("/deleteHistory").hasAnyRole("ADMIN","ASSET_MANAGER")
                            .requestMatchers("/updateHistory").hasAnyRole("ADMIN","ASSET_MANAGER")
                            .requestMatchers("/list/").hasAnyRole("ADMIN", "ASSET_MANAGER")
                            .requestMatchers("/list1/").hasRole("ADMIN")
                            .requestMatchers("/allUpdate").hasRole("ADMIN")
                            .requestMatchers("/allUpdateDemand").hasRole("ASSET_MANAGER")
                            .requestMatchers("/allDelete").hasRole("ADMIN")
                            .requestMatchers("/allDeleteDemand").hasRole("ASSET_MANAGER")
                            .requestMatchers("/asset1/").hasAnyRole("ADMIN", "ASSET_MANAGER", "USER")
                            //asset excel은 아직 하고 있음
                            //AmountSetController
                            .requestMatchers("/getAmountSet").hasRole("ADMIN")
                            .requestMatchers("/changeAmountSet").hasRole("ADMIN")
                            //AssetSurveyController
                            .requestMatchers("/assetSurveyHistory").hasAnyRole("ADMIN", "ASSET_MANAGER")
                            .requestMatchers("/register").hasAnyRole("ADMIN", "ASSET_MANAGER")
                            .requestMatchers("/deleteAssetSurvey").hasAnyRole("ADMIN", "ASSET_MANAGER")
                            .requestMatchers("/assetSurveyDetail/").hasAnyRole("ADMIN", "ASSET_MANAGER")
                            .requestMatchers("/checkLocation/").hasAnyRole("ADMIN", "ASSET_MANAGER")
                            .requestMatchers("/completeSurvey/").hasAnyRole("ADMIN", "ASSET_MANAGER")
                            .requestMatchers("/updateAssetSurveyDetail").hasAnyRole("ADMIN", "ASSET_MANAGER")
                            .requestMatchers("/updateAssetSurveyDetail2").hasAnyRole("ADMIN", "ASSET_MANAGER")
                            //BackUpHistoryController
                            .requestMatchers("/backUpHistory").hasAnyRole("ADMIN", "ASSET_MANAGER")
                            //ChartController
                            .requestMatchers("/chart/**").permitAll()
                            //QR
                            .requestMatchers("/generateQRCode").permitAll()
                            .requestMatchers("/printers/**").permitAll()
                            .requestMatchers("/emailSets/**").permitAll()
                            .requestMatchers("/base64Set/**").permitAll()

                    .anyRequest().authenticated();
                })
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 세션을 사용하지 않음 (JWT)
                )
                //UsernamePasswordAuthentication 필터는 구현 안했는데 어떤 동작을 하는거지
                //이름만 본다면 유저 이름과 패스워드를 인증하는데 컨트롤러에서 하면 안되나?
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("Access token is invalid");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("Refresh token is invalid");
                        })
                );

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
