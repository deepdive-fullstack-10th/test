package org.learn.auth.client;

import org.learn.auth.dto.GithubUserResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GithubApiClient {

    private final RestClient apiClient;

    public GithubApiClient(RestClientGenerator generator) {
        this.apiClient = generator.generateGitHubApiClient();
    }

    public GithubUserResponse getUser(String accessToken) {
        return apiClient.get()
                .uri("/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(GithubUserResponse.class);
    }

}
