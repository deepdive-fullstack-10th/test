package org.learn.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learn.auth.UserAuthRepository;
import org.learn.auth.client.kakao.KakaoApiClient;
import org.learn.auth.client.kakao.KakaoClient;
import org.learn.auth.domain.OAuth2Provider;
import org.learn.auth.domain.UserAuth;
import org.learn.auth.dto.AuthResponse;
import org.learn.auth.dto.KakaoUserResponse;
import org.learn.auth.dto.TokenResponse;
import org.learn.users.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoService {

    private final UserAuthRepository userAuthRepository;
    private final KakaoClient kakaoClient;
    private final KakaoApiClient kakaoApiClient;

    public AuthResponse signUpAndLogin(String code) {
        TokenResponse tokenResponse = kakaoClient.getAccessToken(code);
        KakaoUserResponse kakaoUser = kakaoApiClient.getUser(tokenResponse);

        UserAuth userAuth = findOrSignUp(kakaoUser);

        return new AuthResponse("TOKEN", userAuth.getUser().getNickname(), userAuth.getOAuth2Provider());
    }

    private UserAuth findOrSignUp(KakaoUserResponse kakaoUser) {
        String providerId = "KAKAO_" + kakaoUser.id();
        return userAuthRepository.findById(providerId)
                .orElseGet(() -> {
                    User user = User.createUser(kakaoUser.getNickname());
                    UserAuth userAuth = UserAuth.signUp(providerId, user, OAuth2Provider.KAKAO);
                    return userAuthRepository.save(userAuth);
                });
    }
}
