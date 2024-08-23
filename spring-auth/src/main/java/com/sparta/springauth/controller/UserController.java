package com.sparta.springauth.controller;

import com.sparta.springauth.dto.LoginRequestDto;
import com.sparta.springauth.dto.SignupRequestDto;
import com.sparta.springauth.entity.User;
import com.sparta.springauth.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/user/signup")
    public String signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) { // @ModelAttribute는 생략 가능
        // validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors(); // 오류가 난 field들을 하나씩 가져옴
        if(fieldErrors.size() > 0) {
            for(FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/api/user/signup";
        }
        userService.signup(requestDto);

        return "redirect:/api/user/login-page";
    }

//   **** 이 영역은 JwtAuthenticationFilter에서 구현함
//    @PostMapping("/user/login")
//    public String login(LoginRequestDto requestDto, HttpServletResponse res) {
//        try {
//            userService.login(requestDto, res);
//        } catch (Exception e) { // 로그인 페이지로 넘겨줌
//            return "redirect:/api/user/login-page?error";
//        }
//
//        return "redirect:/";
//    }
}
