package com.broker.OrderService.service.impl;

import com.broker.OrderService.DTO.OrderPlacedEvent;
import com.broker.OrderService.DTO.OrderRequest;
import com.broker.OrderService.DTO.OrderResponse;
import com.broker.OrderService.Entity.Order;
import com.broker.OrderService.mapper.OrderMapper;
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
        Order order = OrderMapper.toEntity(request,Instant.now());
        Order saved = orderRepository.save(order);
        OrderPlacedEvent event = new OrderPlacedEvent(
                saved.getOrderId(), saved.getEmail(), saved.getStockSymbol(),
                saved.getQuantity(), saved.getPrice(), saved.getType(),saved.getTimestamp()
        );
        kafkaTemplate.send("order-placed", event);
        return OrderMapper.toResponse(saved);
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
