package org.learn.common;

import static org.learn.common.SecurityPathConfig.ANONYMOUS_URIS;
import static org.learn.common.SecurityPathConfig.PUBLIC_END_URIS;
import static org.learn.common.SecurityPathConfig.PUBLIC_START_URIS;
import static org.learn.common.SecurityPathConfig.PUBLIC_URIS;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("request ==============>");
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = extractToken(bearerToken);
        setAuthentication(token);

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token) {
        jwtHelper.validationTokenWithThrow(token);
        Long userId = jwtHelper.getUserId(token);

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
        return containsAllowedUris(request) || startsWithAllowedStartUris(request) ||
                matchesGuestRequest(request) || containsAnonymousUris(request);
    }

    private boolean containsAllowedUris(final HttpServletRequest request) {
        return PUBLIC_URIS.stream()
                .anyMatch(url -> request.getRequestURI().contains(url));
    }

    private boolean startsWithAllowedStartUris(final HttpServletRequest request) {
        return PUBLIC_START_URIS.stream()
                .anyMatch(url -> request.getRequestURI().startsWith(url));
    }

    private boolean matchesGuestRequest(final HttpServletRequest request) {
        return PUBLIC_END_URIS.stream()
                .anyMatch(url -> request.getRequestURI().endsWith(url));
    }

    private boolean containsAnonymousUris(final HttpServletRequest request) {
        return ANONYMOUS_URIS.stream()
                .anyMatch(url -> request.getRequestURI().endsWith(url));
    }

}
