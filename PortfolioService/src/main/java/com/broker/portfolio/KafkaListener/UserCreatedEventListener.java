package com.broker.portfolio.KafkaListener;
import com.broker.portfolio.DTO.UserCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventListener {
    @KafkaListener(topics = "user-created-portfolio", groupId = "portfolio-user-created-service", containerFactory = "kafkaUserCreatedListenerContainerFactory")
    public void listen(UserCreatedEvent event){
        System.out.println(event);
    }
}
