package com.broker.portfolio.KafkaListener;
import com.broker.portfolio.DTO.TradePlacedEvent;
import com.broker.portfolio.EventListenerService.StockEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TradeEventListener {

    @Autowired
    private StockEventService stockEventService;

    @KafkaListener(topics = "trade-placed", groupId = "portfolio-trade-service", containerFactory = "kafkaTradeListenerContainerFactory")
    public void listen(TradePlacedEvent trade){
        stockEventService.addTradeToPortfolio(trade);
    }
}
