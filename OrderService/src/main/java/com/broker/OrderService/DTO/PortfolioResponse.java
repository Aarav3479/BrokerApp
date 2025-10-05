package com.broker.OrderService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponse {

    private Long portfolioId;
    private String email;
    private Double totalValue;
    private Instant lastUpdated;
    private Set<StockResponse> stocks;
}
