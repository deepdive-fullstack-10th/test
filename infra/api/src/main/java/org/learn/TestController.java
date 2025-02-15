package org.learn;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final RabbitTemplate rabbitTemplate;

    public TestController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/rabbit-connection")
    public ResponseEntity<String> testRabbitConnection() {
        try {
            rabbitTemplate.convertAndSend("test-exchange", "test-routing-key", "test message");
            return ResponseEntity.ok("RabbitMQ connection successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("RabbitMQ connection failed: " + e.getMessage());
        }
    }
}