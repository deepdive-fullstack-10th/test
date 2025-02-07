package org.learn.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class AnonymousUserFilter extends OncePerRequestFilter {

    public static final Long ANONYMOUS_USER_ID = 0L;

    private final JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("Anonymous UserFilter ==========> uri: {}, header: {}", request.getRequestURI(), request.getHeader(HttpHeaders.AUTHORIZATION));
        Long userId = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .map(this::extractToken)
                .map(jwtHelper::getUserId)
                .orElseGet(() -> {
                    log.info("액세스 토큰 없음. Anonymous User");
                    return ANONYMOUS_USER_ID;
                });

        setAuthentication(userId);
        filterChain.doFilter(request, response);
    }

    private static void setAuthentication(Long userId) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extractToken(String authToken) {
        if (authToken == null || !authToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("토큰 정보가 없습니다.");
        }

        return authToken.substring(7);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return !SecurityPathConfig.ANONYMOUS_URIS.contains(path);
    }

}
