package com.example.security1.controller;

import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 로그인한 사용자에 한해
    @GetMapping("/test/login")
    @ResponseBody
    public String testLogin(Authentication authentication,
                            @AuthenticationPrincipal UserDetails userDetails) { // DI(의존성 주입)
        System.out.println("===============testLogin===============");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication = " + principalDetails.getUser());

        System.out.println("userDetails = " + userDetails.getUsername());

        return "세션 정보 확인하기";
    }

    // 로그인한 사용자에 한해
    @GetMapping("/test/oauth/login")
    @ResponseBody
    public String testOAuthLogin(Authentication authentication,
                                 @AuthenticationPrincipal OAuth2User oauth) { // DI(의존성 주입)
        System.out.println("===============testOAuthLogin===============");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication = " + oAuth2User.getAttributes());

        System.out.println("oauth = " + oauth.getAttributes());

        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping("/")
    public String index() {
        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정: templates (prefix), .mustache (suffix) 생략가능
        return "index";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {

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
