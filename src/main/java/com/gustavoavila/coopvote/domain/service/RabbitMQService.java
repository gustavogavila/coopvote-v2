package com.gustavoavila.coopvote.domain.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

    private final RabbitTemplate rabbitMQTemplate;

    public RabbitMQService(RabbitTemplate rabbitMQTemplate) {
        this.rabbitMQTemplate = rabbitMQTemplate;
    }

    public void sendMessage(String queueName, Object message) {
        this.rabbitMQTemplate.convertAndSend(queueName, message);
    }
}
