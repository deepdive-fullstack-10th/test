package org.learn.worker.codeworker.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learn.worker.codeworker.dto.CodeExecutionMessage;
import org.learn.worker.codeworker.service.CodeExecutionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CodeExecutionConsumer {

    private final CodeExecutionService codeExecutionService;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeMessage(CodeExecutionMessage message) {
        log.info("Received code execution request - executionId: {}, language: {}",
                message.executionId(), message.language());

        codeExecutionService.execute(message);
    }

}
