package com.broker.trade.service;

import com.broker.trade.Component.OrderQueueService;
import com.broker.trade.DTO.OrderPlacedEvent;
import com.broker.trade.DTO.TradePair;
import com.broker.trade.DTO.TradePlacedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;

@Service
public class TradeMatchingService {

    @Autowired
    private OrderQueueService orderQueueService;

    public ResponseEntity<TradePair> matchOrders(OrderPlacedEvent order){

        while(orderQueueService.IsOrderPlaceable(order.getStockSymbol())){
            OrderPlacedEvent buyOrder = orderQueueService.pollBuyOrder(order.getStockSymbol());
            OrderPlacedEvent sellOrder = orderQueueService.pollSellOrder(order.getStockSymbol());
            int quantityTraded = Math.min(buyOrder.getQuantity(),sellOrder.getQuantity());
            double tradePrice = sellOrder.getPrice();           //Most exchanges operate in selling price only
            Instant now = Instant.now();

            int remainingBuy = buyOrder.getQuantity() - quantityTraded;
            int remainingSell = sellOrder.getQuantity() - quantityTraded;
            if (remainingBuy > 0) {
                buyOrder.setQuantity(remainingBuy);
                orderQueueService.offerBuyOrder(buyOrder,order.getStockSymbol());
            }

            if (remainingSell > 0) {
                sellOrder.setQuantity(remainingSell);
                orderQueueService.offerSellOrder(sellOrder,order.getStockSymbol());
            }
            TradePlacedEvent buyResponse = new TradePlacedEvent(
                    buyOrder.getOrderId(),
                    buyOrder.getEmail(),
                    buyOrder.getStockSymbol(),
                    quantityTraded,
                    tradePrice,
                    now,
                    buyOrder.getOrderTimestamp()
            );
            TradePlacedEvent sellResponse = new TradePlacedEvent(
                    sellOrder.getOrderId(),
                    sellOrder.getEmail(),
                    sellOrder.getStockSymbol(),
                    quantityTraded,
                    tradePrice,
                    now,
                    sellOrder.getOrderTimestamp()
            );
            return ResponseEntity.ok(new TradePair(buyResponse,sellResponse));
            //kafka --> portfolio service
            //persist trade

        }
        return null;
    }
}
