package com.example.security1.controller;

import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public String index() {
        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정: templates (prefix), .mustache (suffix) 생략가능
        return "index";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user() {

        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {

        return "admin";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {

        return "manager";
    }

    // 스프링시큐리티 해당주소를 낚아챔 - SecurityConfig 파일 생성 후 작동X
    @GetMapping("/loginForm")
    public String loginForm() {

        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {

        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {

        user.setRole("ROLE_USER");

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }
}
