package org.learn.auth.domain;

public enum OAuth2Provider {

    GITHUB, KAKAO, GOOGLE;

    public static OAuth2Provider from(String provider) {
        try {
            return valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("제공하지 않는 OAuth Login Service 입니다: " + provider);
        }
    }

}
