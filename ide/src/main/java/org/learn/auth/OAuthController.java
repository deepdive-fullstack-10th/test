    package org.learn.auth;

    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.learn.auth.domain.OAuth2Provider;
    import org.learn.auth.dto.AuthResponse;
    import org.learn.auth.service.GithubService;
    import org.learn.auth.service.GoogleService;
    import org.learn.auth.service.KakaoService;
    import org.learn.common.jwt.JwtHelper;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/auth")
    @Slf4j
    @RequiredArgsConstructor
    public class OAuthController {

        private final GithubService githubService;
        private final KakaoService kakaoService;
        private final GoogleService googleService;
        private final JwtHelper jwtHelper;

        @PostMapping("/{provider}/oauth-login")
        public ResponseEntity<AuthResponse> callback(
                @PathVariable String provider,
                @RequestBody OAuthRequest request,
                HttpServletResponse response
        ) {
            log.info(request.toString());
            Long userId = oAuthLoginByProvider(provider, request);
            String accessToken = jwtHelper.issueAccessToken(userId);
            String refreshToken = jwtHelper.issueRefreshToken(userId);

            setRefreshTokenCookie(response, refreshToken);

            return ResponseEntity.ok()
                    .body(new AuthResponse(accessToken));
        }

        private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(isProdEnv());
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24);
            response.addCookie(cookie);
        }

        private Long oAuthLoginByProvider(String provider, OAuthRequest request) {
            return switch (OAuth2Provider.from(provider)) {
                case GITHUB -> githubService.singUpAndLogin(request.oauthCode());
                case KAKAO -> kakaoService.signUpAndLogin(request.oauthCode());
                case GOOGLE -> googleService.signUpAndLogin(request.oauthCode());
            };
        }

        private boolean isProdEnv() {
            String env = System.getenv("SPRING_PROFILES_ACTIVE");
            return env != null && env.equals("prod");
        }

        public record OAuthRequest(String oauthCode) { }

    }