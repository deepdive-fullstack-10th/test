package org.learn.auth.client.github;

import org.learn.auth.client.RestClientGenerator;
import org.learn.auth.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GithubClient {

    private final RestClient restClient;
    private final String clientId;
    private final String clientSecret;

    public GithubClient(
            @Value("${oauth.github.client-id}") String clientId,
            @Value("${oauth.github.client-secret}") String clientSecret,
            RestClientGenerator generator
    ) {
        this.restClient = generator.generateGithubClient();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }


    public TokenResponse getAccessToken(String code) {
        String requestBody = String.format("client_id=%s&client_secret=%s&code=%s", clientId, clientSecret, code);

        return restClient.post()
                .uri("/login/oauth/access_token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(TokenResponse.class);
    }

}
