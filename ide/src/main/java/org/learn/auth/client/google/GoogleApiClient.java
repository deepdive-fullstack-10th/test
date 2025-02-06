package org.learn.auth.client.google;

import org.learn.auth.client.RestClientGenerator;
import org.learn.auth.dto.GoogleUserResponse;
import org.learn.auth.dto.TokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GoogleApiClient {

    private final RestClient apiClient;

    public GoogleApiClient(RestClientGenerator generator) {
        this.apiClient = generator.generateGoogleApiClient();
    }

    public GoogleUserResponse getUser(TokenResponse tokenResponse) {
        return apiClient.get()
                .uri("/oauth2/v3/userinfo")
                .header(HttpHeaders.AUTHORIZATION, tokenResponse.getToken())
                .retrieve()
                .body(GoogleUserResponse.class);
    }

}