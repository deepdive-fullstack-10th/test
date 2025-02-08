package org.learn.ide.producer;


import lombok.extern.slf4j.Slf4j;
import org.learn.ide.IdeController.IdeRequest;
import org.learn.ide.dto.CodeExecutionMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CodeExecutionProducer {


    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public CodeExecutionProducer(
            @Value("${rabbitmq.exchange.name}") String exchangeName,
            @Value("${rabbitmq.routing.key}")String routingKey,
            RabbitTemplate rabbitTemplate
    ) {
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCode(IdeRequest request) {
        CodeExecutionMessage newMessage = CodeExecutionMessage.createNewMessage(request);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, newMessage);
        log.info("Code execution 발행 완료 - {}", request.ideId());
    }

}
