package com.broker.OrderService.service.impl;

import com.broker.OrderService.DTO.*;
import com.broker.OrderService.Entity.Order;
import com.broker.OrderService.FeignClient.PortfolioClient;
import com.broker.OrderService.mapper.OrderMapper;
import com.broker.OrderService.repository.OrderRepository;
import com.broker.OrderService.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private PortfolioClient portfolioClient;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Override
    @CircuitBreaker(name = "PortfolioCircuitBreaker", fallbackMethod = "fallbackResponse")
    public OrderResponse placeOrder(OrderRequest request) {
        PortfolioResponse portfolio = portfolioClient.getPortfolioWithEmail(request.getEmail());
        if(request.getType() == Order.OrderType.SELL){
            StockResponse stockResponse = portfolio.getStocks().stream()
                                               .filter(s -> s.getStockSymbol().equals(request.getStockSymbol()))
                                               .findAny()
                                               .orElseThrow();
            if(stockResponse.getQuantity() < request.getQuantity()){
                throw new RuntimeException("Cannot place sell order for " + request.getStockSymbol() + " above " + stockResponse.getQuantity() + " stocks");
            }
        }
        Order order = OrderMapper.toEntity(request,Instant.now());
        Order saved = orderRepository.save(order);
        OrderPlacedEvent event = new OrderPlacedEvent(
                saved.getOrderId(), saved.getEmail(), saved.getStockSymbol(),
                saved.getQuantity(), saved.getPrice(), saved.getType(),saved.getTimestamp()
        );
        kafkaTemplate.send("order-placed", event);
        return OrderMapper.toResponse(saved);
    }
    public OrderResponse fallbackResponse(OrderRequest request, Exception ex) {
        throw new RuntimeException("Service unavailable: " + ex.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                             .body(ex.getMessage());
    }
    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order->new OrderResponse(order.getOrderId(),order.getEmail(),order.getStockSymbol(),
                        order.getQuantity(),order.getPrice(),order.getType(),order.getTimestamp()))
                .collect(Collectors.toList());
    }
    @Override
    public void deleteOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orderRepository.deleteById(order.getOrderId());
    }
    public void deleteAllOrders(){
        orderRepository.deleteAll();
    }
}
