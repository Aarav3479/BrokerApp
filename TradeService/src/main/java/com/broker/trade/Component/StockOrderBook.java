package com.broker.trade.Component;

import com.broker.trade.DTO.OrderPlacedEvent;
import com.broker.trade.TradeComparators.BuyComparator;
import com.broker.trade.TradeComparators.SellComparator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.PriorityQueue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockOrderBook {

    private PriorityQueue<OrderPlacedEvent> buyQueue = new PriorityQueue<OrderPlacedEvent>(new BuyComparator());
    private PriorityQueue<OrderPlacedEvent> sellQueue = new PriorityQueue<OrderPlacedEvent>(new SellComparator());

}
