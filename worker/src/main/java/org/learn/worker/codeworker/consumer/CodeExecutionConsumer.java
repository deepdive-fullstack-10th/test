package org.learn.worker.codeworker.consumer;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learn.worker.codeworker.client.IdeClient;
import org.learn.worker.codeworker.dto.CodeExecutionMessage;
import org.learn.worker.codeworker.dto.ExecutionResult;
import org.learn.worker.codeworker.dto.ExecutionStatus;
import org.learn.worker.codeworker.service.CodeExecutionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CodeExecutionConsumer {

    private final CodeExecutionService codeExecutionService;
    private final IdeClient ideClient;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeMessage(CodeExecutionMessage message) {
        log.info("Received code execution request - {}", message.toString());

        // result 응답 받고 결과가 성공했다고 가정.
        ExecutionResult resultMessage = codeExecutionService.execute(message);

        // 전송
        ideClient.sendResultToMainServer(resultMessage);
    }

}
