package org.learn.auth.client.kakao;

import org.learn.auth.client.RestClientGenerator;
import org.learn.auth.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoClient {

    private final RestClient authClient;
    private final String clientId;
    private final String clientSecret;

    public KakaoClient(
            RestClientGenerator generator,
            @Value("${oauth.kakao.client-id}") String clientId,
            @Value("${oauth.kakao.client-secret}") String clientSecret
    ) {
        this.authClient = generator.generateKakaoAuthClient();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public TokenResponse getAccessToken(String code) {
        String requestBody = String.format("grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s",
                clientId, clientSecret, code);

        return authClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(requestBody)
                .retrieve()
                .body(TokenResponse.class);
    }

}