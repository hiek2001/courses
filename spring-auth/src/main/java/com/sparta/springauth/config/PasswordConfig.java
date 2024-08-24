package com.sparta.springauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    // 비밀번호 암호화 Bean
    @Bean
    public PasswordEncoder passwordEncoder() {  // Spring Ioc Container에는 pssswordEncoder라는 이름의 Bean 생성되고, 그 안에 BCryptPasswordEncoder() 구현체가 저장됨
        return new BCryptPasswordEncoder(); // Hash 메커니즘을 사용한 강력한 암호화 알고리즘
    }
}
