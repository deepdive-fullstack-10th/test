package org.learn.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learn.auth.UserAuthRepository;
import org.learn.auth.client.github.GithubApiClient;
import org.learn.auth.client.github.GithubClient;
import org.learn.auth.domain.OAuth2Provider;
import org.learn.auth.domain.UserAuth;
import org.learn.auth.dto.GithubUserResponse;
import org.learn.auth.dto.TokenResponse;
import org.learn.users.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GithubService {

    private final UserAuthRepository userAuthRepository;
    private final GithubClient githubClient;
    private final GithubApiClient githubApiClient;

    public Long singUpAndLogin(String oauthCode) {
        TokenResponse tokenResponse = githubClient.getAccessToken(oauthCode);
        GithubUserResponse githubUser = githubApiClient.getUser(tokenResponse);

        UserAuth userAuth = findOrSignUp(githubUser);
        return userAuth.getUser().getId();
    }

    private UserAuth findOrSignUp(GithubUserResponse githubUser) {
        String providerId = "GITHUB_" + githubUser.id();

        return userAuthRepository.findById(providerId).orElseGet(() -> {
            User user = User.createUser(githubUser.login());
            UserAuth userAuth = UserAuth.signUp(providerId, user, OAuth2Provider.GITHUB);
            return userAuthRepository.save(userAuth);
        });
    }

}
