    package org.learn.auth;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.learn.auth.domain.OAuth2Provider;
    import org.learn.auth.dto.AuthResponse;
    import org.learn.auth.service.GithubService;
    import org.learn.auth.service.GoogleService;
    import org.learn.auth.service.KakaoService;
    import org.learn.common.JwtHelper;
    import org.springframework.http.HttpHeaders;
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
                @RequestBody OAuthRequest request
        ) {
            log.info(request.toString());
            Long userId = oAuthLoginByProvider(provider, request);
            String accessToken = jwtHelper.issueAccessToken(userId);
            String refreshToken = jwtHelper.issueRefreshToken(userId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .body(new AuthResponse(userId, refreshToken));
        }

        private Long oAuthLoginByProvider(String provider, OAuthRequest request) {
            return switch (OAuth2Provider.from(provider)) {
                case GITHUB -> githubService.singUpAndLogin(request.oauthCode());
                case KAKAO -> kakaoService.signUpAndLogin(request.oauthCode());
                case GOOGLE -> googleService.signUpAndLogin(request.oauthCode());
            };
        }

        public record OAuthRequest(String oauthCode) { }

    }