package com.broker.trade.TradeComparators;

import com.broker.trade.DTO.OrderPlacedEvent;

import java.util.Comparator;

public class BuyComparator implements Comparator<OrderPlacedEvent> {
    @Override
    public int compare(OrderPlacedEvent o1, OrderPlacedEvent o2) {
        return Double.compare(o1.getPrice(),o2.getPrice());
    }
}
