package com.broker.trade.service;

import com.broker.trade.Component.OrderQueueService;
import com.broker.trade.DTO.OrderPlacedEvent;
import com.broker.trade.DTO.TradePlacedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;

import java.time.Instant;
import java.util.Locale;

public class TradeMatchingService {

    @Autowired
    private OrderQueueService orderQueueService;

    public ResponseEntity<TradePlacedEvent> matchOrders(OrderPlacedEvent order){

        while(orderQueueService.hasMatchingOrders()){

            if(orderQueueService.peekBuyOrder().getPrice()>=orderQueueService.peekSellOrder().getPrice()){
                OrderPlacedEvent buyOrder = orderQueueService.pollBuyOrder();
                OrderPlacedEvent sellOrder = orderQueueService.pollSellOrder();
                int quantityTraded = Math.min(buyOrder.getQuantity(),sellOrder.getQuantity());
                double tradePrice = sellOrder.getPrice();
                Instant now = Instant.now();

                int remainingBuy = buyOrder.getQuantity() - quantityTraded;
                int remainingSell = sellOrder.getQuantity() - quantityTraded;
                if (remainingBuy > 0) {
                    buyOrder.setQuantity(remainingBuy);
                    orderQueueService.offerBuyOrder(buyOrder);
                }

                if (remainingSell > 0) {
                    sellOrder.setQuantity(remainingSell);
                    orderQueueService.offerSellOrder(sellOrder);
                }
                TradePlacedEvent response = new TradePlacedEvent(
                        order.getOrderId(),
                        order.getEmail(),
                        order.getStockSymbol(),
                        order.getQuantity(),
                        order.getPrice(),
                        now,
                        order.getOrderTimestamp()
                );
                return ResponseEntity.ok(response);
                //kafka --> portfolio service
                //persist trade
            }
        }
        return null;
    }
}
