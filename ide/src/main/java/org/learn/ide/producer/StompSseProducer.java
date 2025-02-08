package org.learn.ide.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learn.ide.dto.ExecutionResult;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompSseProducer {

    private static final String EXECUTION_LOCATION_FORMAT = "/sub/executions/%s";

    private final SimpMessagingTemplate messagingTemplate;

    public void publishRunning(String ideId) {
        // 하나의 IDE에 여러사용자가 접속 했을 때, 동시에 실행 결과를 보여주는 것을 고려
        // ide 별 실행 중인 내용을 알려주기 위해 실행 중인 정보도 SSE 처리
        String destination = String.format(EXECUTION_LOCATION_FORMAT, ideId);
        ExecutionResult runningMessage = ExecutionResult.initMessage(ideId);
        messagingTemplate.convertAndSend(destination, runningMessage);
        log.info("ide running message 전송 완료 - {}", ideId);
    }

    public void publishResult(ExecutionResult result) {
        String destination = String.format(EXECUTION_LOCATION_FORMAT, result.executionId());
        messagingTemplate.convertAndSend(destination, result);
        log.info("ide result message 전송 완료 - {}", result.executionId());
    }

}
