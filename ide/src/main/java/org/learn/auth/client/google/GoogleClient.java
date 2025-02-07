package org.learn.auth.client.google;

import org.learn.auth.client.RestClientGenerator;
import org.learn.auth.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GoogleClient {

    private final RestClient authClient;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public GoogleClient(
            RestClientGenerator generator,
            @Value("${oauth.google.client-id}") String clientId,
            @Value("${oauth.google.client-secret}") String clientSecret,
            @Value("${oauth.google.redirect-uri}") String redirectUri
    ) {
        this.authClient = generator.generateGoogleAuthClient();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public TokenResponse getAccessToken(String code) {
        String requestBody = String.format(
                "code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
                code, clientId, clientSecret, redirectUri
        );

        return authClient.post()
                .uri("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(requestBody)
                .retrieve()
                .body(TokenResponse.class);
    }

}