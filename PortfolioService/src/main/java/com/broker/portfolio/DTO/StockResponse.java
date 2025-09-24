package com.broker.portfolio.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockResponse {
    private Long stockId;
    private String stockSymbol;
    private Integer quantity;
    private Double averagePrice;
    private Long portfolioId;
}
