package com.broker.portfolio.service;

import com.broker.portfolio.DTO.PortfolioResponse;
import com.broker.portfolio.Entity.PortfolioStock;

import java.util.List;
import java.util.Set;

public interface PortfolioService {
    PortfolioResponse getPortfolio(Long portfolioId);
    PortfolioResponse getPortfolioWithEmail(String email);
    Set<PortfolioResponse> getAllPortfolios();
    void addStocksToPortfolio(String email, List<PortfolioStock> newStocksList);
    void deletePortfolioById(Long portfolioId);
    void deletePortfolioByEmail(String email);
    void deleteAllPortfolios();
}
