package org.learn.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.learn.auth.domain.OAuth2Provider;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String nickname;
    private OAuth2Provider provider;

}