package com.sparta.springauth.auth;

import com.sparta.springauth.entity.UserRoleEnum;
import com.sparta.springauth.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@RestController
@RequestMapping("/api")
public class AuthController {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Cookie
    @GetMapping("/create-cookie")
    public String createCookie(HttpServletResponse res) {
        addCookie("Ranny Auth", res); // Cookie의 Name - Value

        return "CreateCookie";
    }

    @GetMapping("/get-cookie")
    public String getCookie(@CookieValue(AUTHORIZATION_HEADER) String value) {
        // HttpServletRequest에 담아져 있는 쿠키 중에서 Authorization이라는 이름으로 된 쿠키를 AUTHORIZATION_HEADER 이 어노테이션을 통해서 가지고 온다

        System.out.println("value = "+value);

        return "getCookie : "+value;
    }

    // Session
    @GetMapping("/create-session")
    public String createSession(HttpServletRequest req) {
        // true : 세션이 존재할 경우 세션 반환, 없을 경우에는 새로운 세션을 생성한 후 반환함
        HttpSession session = req.getSession(true);

        // 세션이 저장된 정보 Name - Value를 추가
        session.setAttribute(AUTHORIZATION_HEADER, "Ranny Auth");

        return "createSession";
    }

    @GetMapping("/get-session")
    public String getSession(HttpServletRequest req) {
        // false : 세션이 존재할 경우 세션 반환, 없을 경우 null 반환
        HttpSession session = req.getSession(false);

        // 가져온 세션에 저장된 Value를 Name을 사용하여 가져옴
        String value = (String) session.getAttribute(AUTHORIZATION_HEADER);
        System.out.println("value = "+value);

        return "getSession : "+session;
    }

    // JWT
    @GetMapping("create-jwt")
    public String createJwt(HttpServletResponse res) {
        // jwt 생성
        String token = jwtUtil.createToken("Ranny", UserRoleEnum.USER);

        // jwt 쿠키 저장
        jwtUtil.addJwtToCookie(token, res);

        return "createJwt : "+token;
    }

    @GetMapping("/get-jwt")
    public String getJwt(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 검증
        if(!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        // 토큰에서 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        // 사용자 username
        String username = info.getSubject();
        System.out.println("username = "+username);
        // 사용자 권한
        String authority = (String) info.get(JwtUtil.AUTHORIZATION_KEY);
        System.out.println("authority = "+authority);

        return "getJwt : "+username + ", "+authority;
    }

    public static void addCookie(String cookieValue, HttpServletResponse res) {
        try{
            // HttpServletResponse 객체에 값을 담으면 동적 페이지를 생성한 후에 자연스럽게 클라이언트로 반환됨

            // Cookie Value에는 공백이 불가능하기 때문에 encoding 을 진행해야 함
            cookieValue = URLEncoder.encode(cookieValue, "utf-8").replaceAll("\\+","%20");

            // 공백이 없는 버전의 cookieValue를 가져와서 사용 : Name - Value
            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, cookieValue);
            cookie.setPath("/");
            cookie.setMaxAge(30 * 60);

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);

        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
