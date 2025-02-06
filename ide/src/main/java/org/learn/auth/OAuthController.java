    package org.learn.auth;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.learn.auth.dto.AuthResponse;
    import org.learn.auth.service.GithubService;
    import org.learn.auth.service.GoogleService;
    import org.learn.auth.service.KakaoService;
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

        @PostMapping("/{provider}/oauth-login")
        public ResponseEntity<AuthResponse> callback(
                @PathVariable String provider,
                @RequestBody OAuthRequest request
        ) {
            log.info(request.toString());
            AuthResponse response = switch (provider) {
                case "github" -> githubService.singUpAndLogin(request.oauthCode());
                case "kakao" -> kakaoService.signUpAndLogin(request.oauthCode());
                case "google" -> googleService.signUpAndLogin(request.oauthCode());
                default -> throw new IllegalStateException("제공하지 않는 OAuth Login Service 입니다: " + provider);
            };

            return ResponseEntity.ok(response);
        }

        public record OAuthRequest(String oauthCode) { }

    }