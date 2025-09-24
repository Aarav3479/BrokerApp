package com.broker.portfolio.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "portfolio_stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    private String stockSymbol;

    private Integer quantity;

    private Double averagePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;
}
