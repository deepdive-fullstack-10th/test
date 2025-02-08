package org.learn.ide;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learn.ide.producer.CodeExecutionProducer;
import org.learn.ide.producer.StompSseProducer;
import org.springframework.http.ResponseEntity;
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

    private final StompSseProducer stompSseProducer;
    private final CodeExecutionProducer codeExecutionProducer;

    @PostMapping("/run")
    public ResponseEntity<IdeRunResponse> run(@AuthenticationPrincipal Long userId, @RequestBody IdeRequest dto) {
        // 실제 서비스는 사용자 검증 필요
        stompSseProducer.publishRunning(dto.ideId());
        codeExecutionProducer.publishCode(dto);

        return ResponseEntity.ok(new IdeRunResponse(dto.ideId()));
    }

    public record IdeRequest(String language, String code, String input, String ideId) {}

    public record IdeRunResponse(String executionId) {}

}
