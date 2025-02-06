package org.learn.auth;

import lombok.extern.slf4j.Slf4j;
import org.learn.auth.domain.OAuth2Provider;
import org.learn.auth.domain.UserAuth;
import org.learn.auth.dto.AuthResponse;
import org.learn.auth.dto.GithubUserResponse;
import org.learn.auth.dto.TokenResponse;
import org.learn.users.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/auth")
@Slf4j
public class OAuthController {

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 20000;
    private final RestClient restClient;
    private final RestClient apiClient;
    private final UserAuthRepository userAuthRepository;

    @Value("${oauth.github.client-id}")
    private String clientId;

    @Value("${oauth.github.client-secret}")
    private String clientSecret;

    public OAuthController(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .messageConverters(converters -> {
                    converters.add(new FormHttpMessageConverter());
                    converters.add(new MappingJackson2HttpMessageConverter());
                })
                .baseUrl("https://github.com")
                .build();

        this.apiClient = RestClient.builder()
                .requestFactory(requestFactory)
                .messageConverters(converters -> converters.add(new MappingJackson2HttpMessageConverter()))
                .baseUrl("https://api.github.com")
                .build();
    }

    @GetMapping("/oauth-login")
    public ResponseEntity<AuthResponse> githubCallback(@RequestParam String oauthCode) {
        log.info("[OAuth Controller] GitHub 콜백 요청 수신 - code: {}", oauthCode);

        // 1. Access Token 획득
        TokenResponse tokenResponse = getAccessToken(oauthCode);
        log.info("[OAuth Controller] Access Token 획득 완료");

        // 2. 사용자 정보 조회
        GithubUserResponse userInfo = getUser(tokenResponse.accessToken());
        log.info("[OAuth Controller] GitHub 사용자 정보 조회 완료 - login: {}", userInfo.getLogin());

        // 3. 회원가입 또는 로그인 처리
        UserAuth userAuth = findOrCreateUser(userInfo);
        log.info("[OAuth Controller] 사용자 인증 처리 완료 - nickname: {}", userAuth.getUser().getNickname());

        return ResponseEntity.ok(new AuthResponse("TOKEN", userAuth.getUser().getNickname(), OAuth2Provider.GITHUB));
    }

    private TokenResponse getAccessToken(String code) {
        String requestBody = String.format("client_id=%s&client_secret=%s&code=%s",
                clientId,
                clientSecret,
                code);

        return restClient.post()
                .uri("/login/oauth/access_token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(TokenResponse.class);
    }

    private GithubUserResponse getUser(String accessToken) {
        return apiClient.get()
                .uri("/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(GithubUserResponse.class);
    }

    private UserAuth findOrCreateUser(GithubUserResponse githubUser) {
        String providerId = "GITHUB_" + githubUser.getId();
        return userAuthRepository.findById(providerId)
                .orElseGet(() -> createUser(providerId, githubUser));
    }

    private UserAuth createUser(String providerId, GithubUserResponse githubUser) {
        log.info("[OAuth Controller] 새로운 사용자 생성 - providerId: {}", providerId);
        User user = User.createUser(githubUser.getLogin());
        UserAuth userAuth = UserAuth.signUp(providerId, user, OAuth2Provider.GITHUB);
        return userAuthRepository.save(userAuth);
    }

}