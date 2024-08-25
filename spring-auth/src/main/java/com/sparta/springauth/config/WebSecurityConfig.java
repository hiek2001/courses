package com.sparta.springauth.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    // Spring Security 지원을 가능하게 함
    // Security는 Session 방식으로 동작됨

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                    authorizeHttpRequests
                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                            .requestMatchers("/api/user/**").permitAll() // '/api/user/'로 시작하는 요청 모두 접근 허가
                            .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        // 로그인 사용 : 시큐리티에서 제공하는 로그인 화면을 사용할 수 있음 (default 로그인 화면)
        //http.formLogin(Customizer.withDefaults());

        // 로그인 사용 ( 우리가 만든 로그인 화면을 제공하고 싶을 때 사용)
        http.formLogin((formLogin) ->
                formLogin
                        // 로그인 View 제공 (GET /api/user/login-page)
                        .loginPage("/api/user/login-page")
                        // 로그인 처리 -> 시큐리티 자체에서 제공하는 로그인 처리의 url를 지정할 수 있음
                        // 이전에 controller에서 만들어놨던 로그인 처리의 url이 아님
                        .loginProcessingUrl("/api/user/login")
                        // 로그인 처리 후 성공 시 URL
                        .defaultSuccessUrl("/", true)
                        // 로그인 처리 후 실패 시 URL
                        .failureUrl("/api/user/login-page?error")
                        .permitAll()
        );

        return http.build();
    }
}
