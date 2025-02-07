package org.learn.auth.client.kakao;

import org.learn.auth.client.RestClientGenerator;
import org.learn.auth.dto.KakaoUserResponse;
import org.learn.auth.dto.TokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoApiClient {

    private final RestClient apiClient;

    public KakaoApiClient(RestClientGenerator generator) {
        this.apiClient = generator.generateKakaoApiClient();
    }

    public KakaoUserResponse getUser(TokenResponse tokenResponse) {
        return apiClient.get()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, tokenResponse.getToken())
                .retrieve()
                .body(KakaoUserResponse.class);
    }

}