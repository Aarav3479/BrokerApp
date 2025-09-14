package com.broker.trade.Mapper;

import com.broker.trade.DTO.TradePlacedEvent;
import com.broker.trade.Entity.Trade;

public class TradeMapper {

    public static Trade toEntity(TradePlacedEvent event, long newTradeId){
        Trade trade = new Trade();
        trade.setTradeId(newTradeId);
        trade.setOrderId(event.getOrderId());
        trade.setQuantity(event.getQuantity());
        trade.setPrice(event.getPrice());
        trade.setEmail(event.getEmail());
        trade.setTradeTimestamp(event.getTradeTimestamp());
        trade.setOrderTimestamp(event.getOrderTimestamp());
        trade.setStockSymbol(event.getStockSymbol());
        trade.setOrderType(event.getType());
        return trade;
    }

}
