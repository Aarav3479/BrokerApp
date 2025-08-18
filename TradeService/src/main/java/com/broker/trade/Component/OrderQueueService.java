package com.broker.trade.Component;

import com.broker.trade.DTO.OrderPlacedEvent;
import com.broker.trade.Entity.Trade;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public class OrderQueueService {

    private final Map<String,StockOrderBook> orderBooks = new ConcurrentHashMap<>(); //populate it with all orders in the starting itself

    public void addOrder(OrderPlacedEvent order){
        StockOrderBook book = orderBooks.computeIfAbsent(order.getStockSymbol(),k->(new StockOrderBook()));
        if(order.getType() == Trade.OrderType.BUY){
            book.getBuyQueue().offer(order);
        }
        else{
            book.getSellQueue().offer(order);
        }
    }

    public OrderPlacedEvent pollBuyOrder(String stockSymbol) {
        return orderBooks.get(stockSymbol).getBuyQueue().poll();
    }

    public OrderPlacedEvent pollSellOrder(String stockSymbol) {
        return orderBooks.get(stockSymbol).getSellQueue().poll();
    }

    public void offerBuyOrder(OrderPlacedEvent order, String stockSymbol) {
        orderBooks.get(stockSymbol).getBuyQueue().offer(order);
    }

    public void offerSellOrder(OrderPlacedEvent order, String stockSymbol) {
        orderBooks.get(stockSymbol).getSellQueue().offer(order);
    }
    public boolean IsOrderPlaceable(String stockSymbol) {
        if(orderBooks.get(stockSymbol).getBuyQueue().isEmpty() || orderBooks.get(stockSymbol).getSellQueue().isEmpty()){
            return false;
        }
        if(orderBooks.get(stockSymbol).getBuyQueue().peek().getPrice()>=orderBooks.get(stockSymbol).getSellQueue().peek().getPrice()){
            return true;
        }
        return false;
    }

    public OrderPlacedEvent peekBuyOrder(String stockSymbol) {
        return orderBooks.get(stockSymbol).getBuyQueue().peek();
    }

    public OrderPlacedEvent peekSellOrder(String stockSymbol) {
        return orderBooks.get(stockSymbol).getSellQueue().peek();
    }
}
