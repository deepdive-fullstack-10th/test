package org.learn.worker.codeworker.client;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learn.worker.codeworker.dto.ExecutionResult;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class IdeClient {

    private final WebClient webClient;

    public void sendResultToMainServer(ExecutionResult result) {
        webClient.post()
                .uri("/ide/result")
                .bodyValue(result)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(success -> log.info("결과 전달 성공 : {}", result.toString()))
                .doOnError(throwable -> {
                    log.error("왜 실패했지? - executionId: {}", result.executionId(), throwable);
                })
                .subscribe();
    }

}
