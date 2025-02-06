package org.learn.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learn.auth.UserAuthRepository;
import org.learn.auth.client.GithubApiClient;
import org.learn.auth.client.GithubClient;
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

    public UserAuth singUpAndLogin(String oauthCode) {
        TokenResponse tokenResponse = githubClient.getAccessToken(oauthCode);
        GithubUserResponse githubUser = githubApiClient.getUser(tokenResponse.accessToken());

        return findOrSignUp(githubUser);
    }

    private UserAuth findOrSignUp(GithubUserResponse githubUser) {
        String providerId = "GITHUB_" + githubUser.getId();

        return userAuthRepository.findById(providerId).orElseGet(() -> {
            User user = User.createUser(githubUser.getLogin());
            UserAuth userAuth = UserAuth.signUp(providerId, user, OAuth2Provider.GITHUB);
            return userAuthRepository.save(userAuth);
        });
    }

}
