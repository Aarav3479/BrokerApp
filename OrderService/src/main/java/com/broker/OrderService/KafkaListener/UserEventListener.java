package com.broker.OrderService.KafkaListener;


import com.broker.OrderService.DTO.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserEventListener {
    @KafkaListener(topics = "user-created", groupId = "order-service",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleUserCreated(UserCreatedEvent event) {
        log.info("Received UserCreatedEvent: {}", event);
    }
}