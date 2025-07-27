package com.broker.trade.Component;

import com.broker.trade.DTO.OrderPlacedEvent;
import com.broker.trade.Entity.trade;
import com.broker.trade.TradeComparators.SellComparator;
import lombok.Getter;
import org.springframework.stereotype.Component;
import com.broker.trade.TradeComparators.BuyComparator;

import java.util.Comparator;
import java.util.PriorityQueue;

@Component
@Getter
public class OrderQueues {

    private final PriorityQueue<OrderPlacedEvent> buyQueue = new PriorityQueue<OrderPlacedEvent>(new BuyComparator());
    private final PriorityQueue<OrderPlacedEvent> sellQueue = new PriorityQueue<OrderPlacedEvent>(new SellComparator());

    public void addOrder(OrderPlacedEvent order){
        if(order.getType() == trade.OrderType.BUY){
            buyQueue.add(order);
        }
        else{
            sellQueue.add(order);
        }
    }
}
