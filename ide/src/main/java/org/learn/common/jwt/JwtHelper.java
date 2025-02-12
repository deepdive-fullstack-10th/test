package org.learn.common.jwt;

public interface JwtHelper {

    String issueAccessToken(Long userid);
    String issueRefreshToken(Long userid);
    void validationTokenWithThrow(String token);
    Long getUserId(String token);

}
