package org.learn.ide;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ide")
@Slf4j
public class IdeController {

    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/run")
    public ResponseEntity<IdeRunResponse> run(@AuthenticationPrincipal Long userId, @RequestBody IdeRequest dto) {
        // 실제 서비스는 사용자 검증 필요

        // 여러 탭에서 실행 하는 것을 고려해서 실행 중인 내용을 알려주는 메세지 작성
        messagingTemplate.convertAndSend("/sub/executions/" + dto.ideId(), ExecutionResult.initMessage(dto.ideId()));
        log.info("ide run message 전송 완료");

        // rabbitMQ 메시징
        // todo execution시 ExecutionResult 의 status 가 SUCCESS or FAIL이 되어야 함.

        return ResponseEntity.ok(new IdeRunResponse(dto.ideId()));
    }

    public record IdeRequest(String language, String code, String input, String ideId) {}

    public record IdeRunResponse(String executionId) {}

}
