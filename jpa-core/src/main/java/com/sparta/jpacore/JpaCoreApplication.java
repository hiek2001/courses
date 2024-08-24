package com.sparta.jpacore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JpaCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaCoreApplication.class, args);
    }

}
