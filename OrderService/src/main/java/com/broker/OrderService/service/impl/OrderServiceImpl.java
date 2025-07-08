package com.broker.OrderService.service.impl;

import com.broker.OrderService.DTO.OrderPlacedEvent;
import com.broker.OrderService.DTO.OrderRequest;
import com.broker.OrderService.DTO.OrderResponse;
import com.broker.OrderService.Entity.Order;
import com.broker.OrderService.repository.OrderRepository;
import com.broker.OrderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Override
    public OrderResponse placeOrder(OrderRequest request) {
        Order order = Order.builder()
                .userId(request.getUserId())
                .stockSymbol(request.getStockSymbol())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .type(request.getType())
                .timestamp(Instant.now())
                .build();

        Order saved = orderRepository.save(order);
        OrderPlacedEvent event = new OrderPlacedEvent(
                saved.getOrderId(), saved.getUserId(), saved.getStockSymbol(),
                saved.getQuantity(), saved.getPrice(), saved.getType(),saved.getTimestamp()
        );
        kafkaTemplate.send("order-placed", event);
        return toResponse(saved);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse res = new OrderResponse();
        res.setOrderId(order.getOrderId());
        res.setUserId(order.getUserId());
        res.setStockSymbol(order.getStockSymbol());
        res.setQuantity(order.getQuantity());
        res.setPrice(order.getPrice());
        res.setType(order.getType());
        res.setTimestamp(order.getTimestamp());
        return res;
    }
}
