package org.learn;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class RabbitConfig {

    private Environment env;
    public RabbitConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public Queue testQueue() {
        return new Queue("test-queue", true);
    }

    @Bean
    public DirectExchange testExchange() {
        return new DirectExchange("test-exchange");
    }

    @Bean
    public Binding binding(Queue testQueue, DirectExchange testExchange) {
        return BindingBuilder.bind(testQueue)
                .to(testExchange)
                .with("test-routing-key");
    }

    @PostConstruct
    public void logConfig() {
        log.info("RabbitMQ Configuration - Host: {}, Port: {}, Username: {}, password: {}",
                env.getProperty("spring.rabbitmq.host"),
                env.getProperty("spring.rabbitmq.port"),
                env.getProperty("spring.rabbitmq.username"),
                env.getProperty("spring.rabbitmq.password")
        );
    }

}
