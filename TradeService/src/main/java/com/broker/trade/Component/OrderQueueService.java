package com.broker.trade.Component;

import com.broker.trade.DTO.OrderPlacedEvent;
import com.broker.trade.Entity.trade;
import com.broker.trade.TradeComparators.SellComparator;
import lombok.Getter;
import org.springframework.stereotype.Component;
import com.broker.trade.TradeComparators.BuyComparator;

import java.util.PriorityQueue;

@Component
@Getter
public class OrderQueueService {

    private final PriorityQueue<OrderPlacedEvent> buyQueue = new PriorityQueue<OrderPlacedEvent>(new BuyComparator());
    private final PriorityQueue<OrderPlacedEvent> sellQueue = new PriorityQueue<OrderPlacedEvent>(new SellComparator());

    public OrderPlacedEvent pollBuyOrder() {
        return buyQueue.poll();
    }

    public OrderPlacedEvent pollSellOrder() {
        return sellQueue.poll();
    }

    public void offerBuyOrder(OrderPlacedEvent order) {
        buyQueue.offer(order);
    }

    public void offerSellOrder(OrderPlacedEvent order) {
        sellQueue.offer(order);
    }
    public boolean hasMatchingOrders() {
        return !buyQueue.isEmpty() && !sellQueue.isEmpty();
    }

    public OrderPlacedEvent peekBuyOrder() {
        return buyQueue.peek();
    }

    public OrderPlacedEvent peekSellOrder() {
        return sellQueue.peek();
    }
    public void addOrder(OrderPlacedEvent order){
        if(order.getType() == trade.OrderType.BUY){
            buyQueue.add(order);
        }
        else{
            sellQueue.add(order);
        }
    }
}
