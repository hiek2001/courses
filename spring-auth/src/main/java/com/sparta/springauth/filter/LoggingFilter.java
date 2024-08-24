package com.sparta.springauth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "LoggingFilter")
@Component
@Order(1)
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // FilterChain : filter를 이동하기 위해 받아오는 것

        // 전처리 : 어떤 로그인지 먼저 로그를 찍음
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();
        log.info(url);

        chain.doFilter(request, response); // 다음 Filter 로 이동

        // 후처리 : DispatcherServlet를 통해 실행된 모든 로직이 완료된 일련의 결과물이 filter를 한번 더 들어옴
        log.info("비즈니스 로직 완료");
    }
}