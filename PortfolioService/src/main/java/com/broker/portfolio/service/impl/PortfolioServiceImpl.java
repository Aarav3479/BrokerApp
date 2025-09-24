package com.broker.portfolio.service.impl;

import com.broker.portfolio.DTO.PortfolioResponse;
import com.broker.portfolio.Entity.Portfolio;
import com.broker.portfolio.Mapper.EntityMapper;
import com.broker.portfolio.repository.PortfolioRepository;
import com.broker.portfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Override
    public PortfolioResponse getPortfolio(Long portfolioId) {
        Optional<Portfolio> portfolio = portfolioRepository.findByIdWithStocks(portfolioId);
        if(portfolio.isEmpty()){
            throw new RuntimeException("Portfolio doesn't exist");
        }
        return EntityMapper.EntityToPortfolioResponse(portfolio.get());
    }

    @Override
    public List<PortfolioResponse> getAllPortfolios() {
        List<Portfolio> portfolioList = portfolioRepository.findAllWithStocks();
        return portfolioList.stream()
                .map(EntityMapper::EntityToPortfolioResponse)
                .toList();
    }

    @Override
    public void deletePortfolio(Long portfolioId) {
        portfolioRepository.deleteById(portfolioId);
    }

    @Override
    public void deleteAllPortfolios() {
        portfolioRepository.deleteAll();
    }
}
