package com.sparta.springauth.jwt;

import com.sparta.springauth.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    // 다른 객체에 의존하지 않고 하나의 모듈로써 동작하는 Class

    // Header Key 값, Cookie의 Name 값 (Cookie : Name - Value 구조)
    public static final String AUTHORIZATION_HEADER="Authorization";
    // 사용자 권한 값의 Key (권한 구분 및 가져오기 위한 Key 값 : 비밀번호와 같은 위험한 내용은 아니기 때문에 Header에 같이 보내기도 함)
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자 (규칙 : Bearer이 붙여져있으면 뒤에 오는 값은 token이라는 뜻)
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분 (밀리세컨드 단위)

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;    // secret key를 담을 객체
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    // @PostConstruct : 딱 한번을 받아오면 되는 값을 받아올 때마다, 요청을 새로 호출하는 실수를 방지하기 위해서 사용
    @PostConstruct
    public void init() {
        // 생성자가 만들어진 뒤에 key를 만들어서 붙여줌
        // secret key를 decoding 한 byte타입의 값을 key로 만들어줌
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)   // 사용자 식별자 값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한(KEY-VALUE)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))   // 만료 시간
                        .setIssuedAt(date)  // 발급일
                        .signWith(key, signatureAlgorithm)  // 암호화 알고리즘
                        .compact();
    }

    // JWT Cookie에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            // Cookie Value에는 공백이 불가능해서 encoding 진행
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+","%20");
            
            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);
            cookie.setPath("/");
            
            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // JWT 토큰 substring : JWT 토큰 앞에 붙여져있는 'Bearer '을 떼기 위한 것
    public String substringToken(String tokenValue) {
        if(StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // JWT 토큰 검증
    public boolean validateToken(String token) {
        try {
            // setSigningKey : 암호화할 때 사용한 그 key
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(SecurityException | MalformedJwtException e) {
            logger.error("Invalid JWT signature, 유효하지 않은 JWT 서명입니다.");
        } catch(ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch(UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않은 JWT 토큰입니다.");
        } catch(IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // JWT 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        // Jwts.parserBuilder() : secret key를 사용하여 JWT의 Claims를 가져와 담겨 있는 사용자 정보를 사용
        // setSigningKey : 토큰에서 검증할 때 사용했던 secret key
        // parseClamisJws : 분석할 token
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // @CookieValue 어노테이션을 사용못하기 때문에 만들었음. Filter는 Spring Run 시, 최초 실행되는 것이기 때문에.
    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
