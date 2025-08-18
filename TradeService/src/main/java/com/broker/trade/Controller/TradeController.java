package com.broker.trade.Controller;

import com.broker.trade.Entity.Trade;
import com.broker.trade.service.TradeMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
@RequiredArgsConstructor
public class TradeController {
    @Autowired
    private TradeMatchingService tradeMatchingService;

    @GetMapping
    public ResponseEntity<List<Trade>> getAllUsers() {
        List<Trade> users = tradeMatchingService.getAllTrades();
        return ResponseEntity.ok(users);
    }
}
