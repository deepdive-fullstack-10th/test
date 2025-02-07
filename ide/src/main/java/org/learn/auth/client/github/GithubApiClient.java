package org.learn.auth.client.github;

import org.learn.auth.client.RestClientGenerator;
import org.learn.auth.dto.GithubUserResponse;
import org.learn.auth.dto.TokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GithubApiClient {

    private final RestClient apiClient;

    public GithubApiClient(RestClientGenerator generator) {
        this.apiClient = generator.generateGitHubApiClient();
    }

    public GithubUserResponse getUser(TokenResponse tokenResponse) {
        return apiClient.get()
                .uri("/user")
                .header(HttpHeaders.AUTHORIZATION, tokenResponse.getToken())
                .retrieve()
                .body(GithubUserResponse.class);
    }

}
