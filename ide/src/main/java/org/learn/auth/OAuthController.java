    package org.learn.auth;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.learn.auth.domain.OAuth2Provider;
    import org.learn.auth.domain.UserAuth;
    import org.learn.auth.dto.AuthResponse;
    import org.learn.auth.service.GithubService;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/auth")
    @Slf4j
    @RequiredArgsConstructor
    public class OAuthController {

        private final GithubService githubService;

        @PostMapping("/oauth-login")
        public ResponseEntity<AuthResponse> githubCallback(@RequestBody GithubAuthRequest request) {
            log.info("[OAuth Controller] GitHub 콜백 요청 수신 - code: {}", request.oauthCode);
            UserAuth userAuth = githubService.singUpAndLogin(request.oauthCode);
            return ResponseEntity.ok(new AuthResponse("TOKEN", userAuth.getUser().getNickname(), OAuth2Provider.GITHUB));
        }

        public record GithubAuthRequest(String oauthCode) { }

    }