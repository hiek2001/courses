package com.sparta.springauth.config;

import com.sparta.springauth.jwt.JwtAuthenticationFilter;
import com.sparta.springauth.jwt.JwtAuthorizationFilter;
import com.sparta.springauth.jwt.JwtUtil;
import com.sparta.springauth.security.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    // Spring Security 지원을 가능하게 함
    // Security는 Session 방식으로 동작됨

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }


    // authenticationManager를 만들고 등록함
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        // login할 때 사용하는 url를 등록해야하기 때문에 jwtUtil 넣는 것
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        // 로그인을 할때마다 session id가 생성되지 않음. 로그인이 완료됐을 경우에만 session id가 생성됨
        http.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                    authorizeHttpRequests
                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                            .requestMatchers("/api/user/**").permitAll() // '/api/user/'로 시작하는 요청 모두 접근 허가
                            .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        // 로그인 사용 : 시큐리티에서 제공하는 로그인 화면을 사용할 수 있음 (default 로그인 화면)
        //http.formLogin(Customizer.withDefaults());

        // 로그인 사용 ( 우리가 만든 로그인 화면을 제공하고 싶을 때 사용)
//        http.formLogin((formLogin) ->
//                formLogin
//                        // 로그인 View 제공 (GET /api/user/login-page)
//                        .loginPage("/api/user/login-page")
//                        // 로그인 처리 -> 시큐리티 자체에서 제공하는 로그인 처리의 url를 지정할 수 있음
//                        // 이전에 controller에서 만들어놨던 로그인 처리의 url이 아님
//                        .loginProcessingUrl("/api/user/login")
//                        // 로그인 처리 후 성공 시 URL
//                        .defaultSuccessUrl("/", true)
//                        // 로그인 처리 후 실패 시 URL
//                        .failureUrl("/api/user/login-page?error")
//                        .permitAll()
//        );

        http.formLogin((formLogin) ->
            formLogin
                    .loginPage("/api/user/login-page").permitAll()
        );


        // 필터 관리
        // 위에서 만들어놓은 filter를 아래의 코드를 사용하여 filterChain에 넣어주기 위한 것
        // 해당 filter들이 어떤 순서로 진행할 것인지

        // filterChain 실행되는 순서
        // AuthorizationFilter() -> AuthenticationFilter() -> UsernamePasswordAuthenticationFilter()
        // 인가 -> 로그인 -> username, password 확인
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
