package com.broker.portfolio.KafkaListener;
import com.broker.portfolio.DTO.UserDeletedEvent;
import com.broker.portfolio.EventListenerService.PortfolioEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserDeletedEventListener {

    @Autowired
    private PortfolioEventService portfolioEventService;

    @KafkaListener(topics = "user-deleted", groupId = "portfolio-user-deleted-service", containerFactory = "kafkaUserDeletedListenerContainerFactory")
    public void listen(UserDeletedEvent event){
        portfolioEventService.deletePortfolio(event);
    }
}
