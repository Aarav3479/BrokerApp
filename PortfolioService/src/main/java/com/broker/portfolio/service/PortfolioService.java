package com.broker.portfolio.service;

import com.broker.portfolio.DTO.PortfolioResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PortfolioService {
    PortfolioResponse getPortfolio(Long portfolioId);
    List<PortfolioResponse> getAllPortfolios();
    void deletePortfolio(Long portfolioId);
    void deleteAllPortfolios();
}
