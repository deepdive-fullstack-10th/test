package org.learn.common.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.learn.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtHelperImpl implements JwtHelper {

    private final SecretKey secretKey;
    private final long accessTokenExp;
    private final long refreshTokenExp;

    public JwtHelperImpl(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.access-exp}") long accessTokenPlusHour,
            @Value("${jwt.refresh-exp}") long refreshTokenPlusHour
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExp = accessTokenPlusHour;
        this.refreshTokenExp = refreshTokenPlusHour;
    }

    public String issueAccessToken(Long userid) {
        final Date now = DateUtils.getDate();
        final Date expiryDate = DateUtils.getDate(now.getTime() + accessTokenExp);

        return Jwts.builder()
                .signWith(secretKey)
                .subject(String.valueOf(userid))
                .expiration(expiryDate)
                .issuedAt(now)
                .compact();
    }

    public String issueRefreshToken(final Long userid) {
        final Date now = DateUtils.getDate();
        final Date expiryDate = DateUtils.getDate(now.getTime() + refreshTokenExp);

        return Jwts.builder()
                .signWith(secretKey)
                .subject(String.valueOf(userid))
                .expiration(expiryDate)
                .issuedAt(now)
                .compact();
    }

    public void validationTokenWithThrow(final String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (final ExpiredJwtException e) {
            throw new IllegalArgumentException("토큰 유효 기간 만료");
        } catch (final SignatureException e) {
            throw new IllegalArgumentException("유효하지 않는 토큰 서명");
        }  catch (final UnsupportedJwtException e) {
            throw new IllegalArgumentException("지원하지 않는 토큰 유형");
        } catch (final MalformedJwtException e) {
            throw new IllegalArgumentException("잘못된 형식의 토큰");
        } catch (final Exception e) {
            throw new IllegalArgumentException("알 수 없는 토큰 오류");
        }
    }

    public Long getUserId(String token) {
        try {
            String subject = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

            return Long.valueOf(subject);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("사용자 ID를 찾을 수 없습니다.");
        }
    }

}
