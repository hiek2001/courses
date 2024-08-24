package com.sparta.springauth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("수동 등록한 passwordEncoder를 주입 받아와 문자열 암호화")
    void test1() {
        String password = "Ranny's password";

        // 암호화
        String encodePassword = passwordEncoder.encode(password);
        System.out.println("encodePassword = "+encodePassword); // 실제 암호화가 되는지 확인

        String inputPassword = "Ranny";

        // 복호화를 통해 암호화된 비밀번호와 비교
        boolean matches = passwordEncoder.matches(inputPassword, encodePassword);
        System.out.println("matches = "+matches); // 암호화할 때 사용된 값과 다른 문자열과 비교했으면 false
    }

}
