package com.broker.portfolio.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portfolioId;
    private Double totalValue;
    private String email;
    private Instant lastUpdated;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true ,fetch = FetchType.LAZY, mappedBy = "portfolio")
    private Set<PortfolioStock> stocks;
}
