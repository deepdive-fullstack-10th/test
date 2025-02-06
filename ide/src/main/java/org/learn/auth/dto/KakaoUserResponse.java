package org.learn.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUserResponse(
        Long id,
        KakaoAccount kakaoAccount,
        Properties properties
) {

    public String getNickname() {
        return properties.nickname();
    }

    public record KakaoAccount(
            String email,
            Boolean emailVerified
    ) {}

    public record Properties(
            String nickname
    ) {}

}