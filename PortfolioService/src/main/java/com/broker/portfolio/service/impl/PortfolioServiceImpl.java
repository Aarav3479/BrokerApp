package com.broker.portfolio.service.impl;

import com.broker.portfolio.DTO.PortfolioResponse;
import com.broker.portfolio.Entity.Portfolio;
import com.broker.portfolio.Mapper.EntityMapper;
import com.broker.portfolio.repository.PortfolioRepository;
import com.broker.portfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
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
    public Set<PortfolioResponse> getAllPortfolios() {
        return portfolioRepository.findAllWithStocks()
                .stream()
                .map(EntityMapper::EntityToPortfolioResponse)
                .collect(Collectors.toSet());
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
