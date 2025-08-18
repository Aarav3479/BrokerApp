package com.broker.trade.service;

import com.broker.trade.Component.OrderQueueService;
import com.broker.trade.DTO.OrderPlacedEvent;
import com.broker.trade.Mapper.TradeMapper;
import com.broker.trade.DTO.TradePair;
import com.broker.trade.DTO.TradePlacedEvent;
import com.broker.trade.Entity.Trade;
import com.broker.trade.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;

@Service
public class TradeMatchingService {

    @Autowired
    private OrderQueueService orderQueueService;
    @Autowired
    private TradeRepository tradeRepository;

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
                    Trade.OrderType.BUY,
                    now,
                    buyOrder.getOrderTimestamp()
            );
            TradePlacedEvent sellResponse = new TradePlacedEvent(
                    sellOrder.getOrderId(),
                    sellOrder.getEmail(),
                    sellOrder.getStockSymbol(),
                    quantityTraded,
                    tradePrice,
                    Trade.OrderType.SELL,
                    now,
                    sellOrder.getOrderTimestamp()
            );
            Trade buyEntity = TradeMapper.toEntity(buyResponse);
            Trade sellEntity = TradeMapper.toEntity(sellResponse);
            tradeRepository.save(buyEntity);
            tradeRepository.save(sellEntity);
            return ResponseEntity.ok(new TradePair(buyResponse,sellResponse));

            //kafka --> portfolio service

        }
        return null;
    }
    public List<Trade> getAllTrades(){
        return tradeRepository.findAll();
    }
}
