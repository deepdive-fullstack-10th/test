package org.learn.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learn.auth.UserAuthRepository;
import org.learn.auth.client.google.GoogleApiClient;
import org.learn.auth.client.google.GoogleClient;
import org.learn.auth.domain.OAuth2Provider;
import org.learn.auth.domain.UserAuth;
import org.learn.auth.dto.AuthResponse;
import org.learn.auth.dto.GoogleUserResponse;
import org.learn.auth.dto.TokenResponse;
import org.learn.users.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GoogleService {

    private final UserAuthRepository userAuthRepository;
    private final GoogleClient googleClient;
    private final GoogleApiClient googleApiClient;

    public AuthResponse signUpAndLogin(String code) {
        TokenResponse tokenResponse = googleClient.getAccessToken(code);
        GoogleUserResponse googleUser = googleApiClient.getUser(tokenResponse);

        UserAuth userAuth = findOrSignUp(googleUser);

        return new AuthResponse("TOKEN", userAuth.getUser().getNickname(), userAuth.getOAuth2Provider());
    }

    private UserAuth findOrSignUp(GoogleUserResponse googleUser) {
        String providerId = "GOOGLE_" + googleUser.sub();
        return userAuthRepository.findById(providerId)
                .orElseGet(() -> {
                    User user = User.createUser(googleUser.name());
                    UserAuth userAuth = UserAuth.signUp(providerId, user, OAuth2Provider.GOOGLE);
                    return userAuthRepository.save(userAuth);
                });
    }
}
