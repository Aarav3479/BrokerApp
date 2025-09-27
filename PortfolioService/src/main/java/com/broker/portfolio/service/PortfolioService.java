package com.broker.portfolio.service;

import com.broker.portfolio.DTO.PortfolioResponse;
import java.util.Set;

public interface PortfolioService {
    PortfolioResponse getPortfolio(Long portfolioId);
    Set<PortfolioResponse> getAllPortfolios();
    void deletePortfolio(Long portfolioId);
    void deleteAllPortfolios();
}
