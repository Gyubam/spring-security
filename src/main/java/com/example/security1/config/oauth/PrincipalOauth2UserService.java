package com.example.security1.config.oauth;

import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.config.oauth.provider.FacebookUserInfo;
import com.example.security1.config.oauth.provider.GoogleUserInfo;
import com.example.security1.config.oauth.provider.OAuth2UserInfo;
import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;


    // 구글로 부터 받은 userRequest 데이터에 대해 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어짐
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("userRequest.getClientRegistration() = " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인가능
        System.out.println("userRequest.getAccessToken().getTokenValue() = " + userRequest.getAccessToken().getTokenValue());
        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료
        // -> code를 리턴(OAuth-client 라이브러리) -> AccessToken요청
        // userRequest 정보 -> 회원프로필 받아야함(loadUser) -> 구글로부터 회원프로필

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("getAttributes() = " + oAuth2User.getAttributes());

        // 회원가입 진행
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());

        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());

        }else {
            System.out.println("구글 페이스북만 지원");
        }

        String provider = oAuth2UserInfo.getProvider(); // google
        String provideId = oAuth2UserInfo.getProviderId(); //
        String username = provider + "_" + provideId; // google_10128412491824
        String email = oAuth2UserInfo.getEmail();
        String password = bCryptPasswordEncoder.encode("겟인데어"); // 크게 의미없음, 패스워드는 필요 x
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .provideId(provideId)
                    .build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
        // Authentication 객체에 들어감
    }

}
