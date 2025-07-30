package com.broker.trade.KafkaListener;

import com.broker.trade.Component.OrderQueueService;
import com.broker.trade.DTO.OrderPlacedEvent;
import com.broker.trade.service.TradeMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    @Autowired
    private OrderQueueService orderQueueService;
    @Autowired
    private TradeMatchingService tradeMatchingService;

    @KafkaListener(topics = "order-placed", groupId = "trade-service", containerFactory = "kafkaListenerContainerFactory")
    public void listen(OrderPlacedEvent order){
        orderQueueService.addOrder(order);
        tradeMatchingService.matchOrders(order);
    }
}
