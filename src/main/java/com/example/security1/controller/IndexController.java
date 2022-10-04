package com.example.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정: templates (prefix), .mustache (suffix) 생략가능
        return "index";
    }

    @GetMapping("/user")
    public String user() {

        return "user";
    }

    @GetMapping("/admin")
    public String admin() {

        return "admin";
    }

    @GetMapping("/manager")
    public String manager() {

        return "manager";
    }

    // 스프링시큐리티 해당주소를 낚아챔 - SecurityConfig 파일 생성 후 작동X
    @GetMapping("/loginForm")
    public String login() {

        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String join() {

        return "joinForm";
    }

    @GetMapping("/joinProc")
    @ResponseBody
    public String joinProc() {

        return "회원가입 완료됨";
    }
}
