package com.broker.portfolio.service.impl;

import com.broker.portfolio.DTO.PortfolioResponse;
import com.broker.portfolio.Entity.Portfolio;
import com.broker.portfolio.Entity.PortfolioStock;
import com.broker.portfolio.Mapper.EntityMapper;
import com.broker.portfolio.repository.PortfolioRepository;
import com.broker.portfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
    public PortfolioResponse getPortfolioWithEmail(String email){
        Optional<Portfolio> portfolio = portfolioRepository.findByEmailWithStocks(email);
        if(portfolio.isEmpty()){
            throw new RuntimeException("Portfolio with the given email doesn't exist");
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
    public void addStocksToPortfolio(String email, List<PortfolioStock> newStocksList){
        Optional<Portfolio> portfolio = portfolioRepository.findByEmailWithStocks(email);
        if(portfolio.isEmpty()){
            throw new RuntimeException("Portfolio with the email " + email + " not found");
        }
        double newPortfolioValue = portfolio.get().getTotalValue();
        for (PortfolioStock stock : newStocksList) {
            newPortfolioValue += (stock.getAveragePrice()*stock.getQuantity());
            stock.setPortfolio(portfolio.get());
            portfolio.get().getStocks().add(stock);
        }
        portfolio.get().setTotalValue(newPortfolioValue);
        portfolio.get().setLastUpdated(Instant.now());
        portfolioRepository.save(portfolio.get());
    }
    @Override
    public void deletePortfolioById(Long portfolioId) {
        portfolioRepository.deleteById(portfolioId);
    }
    @Override
    public void deletePortfolioByEmail(String email) {
        portfolioRepository.deleteByEmail(email);
    }
    @Override
    public void deleteAllPortfolios() {
        portfolioRepository.deleteAll();
    }
}
