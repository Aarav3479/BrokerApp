package com.broker.portfolio.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponse {

    private Long portfolioId;
    private String email;
    private Double totalValue;
    private Instant lastUpdated;
    private List<StockResponse> stocks;
}
