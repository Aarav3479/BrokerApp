package com.broker.portfolio.KafkaListener;

import com.broker.portfolio.DTO.TradePlacedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TradeEventListener {
    @KafkaListener(topics = "trade-placed", groupId = "portfolio-trade-service", containerFactory = "kafkaTradeListenerContainerFactory")
    public void listen(TradePlacedEvent trade){
        System.out.println(trade);
    }
}
