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
import java.util.function.Function;
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
    @Transactional
    public void addStocksToPortfolio(String email, List<PortfolioStock> incoming) {
        Portfolio p = portfolioRepository.findByEmailWithStocks(email)
                .orElseThrow(() -> new RuntimeException("Portfolio with the email " + email + " not found"));

        Map<String, PortfolioStock> bySym = p.getStocks().stream()
                .collect(Collectors.toMap(PortfolioStock::getStockSymbol, Function.identity()));

        Map<String, Integer> addQty = new HashMap<>();
        Map<String, Double> addCost = new HashMap<>(); //Two maps to deal with multiple entries of a symbol
        for (PortfolioStock s : incoming) {
            addQty.merge(s.getStockSymbol(), s.getQuantity(), Integer::sum);
            addCost.merge(s.getStockSymbol(), s.getAveragePrice() * s.getQuantity(), Double::sum);
        }

        Instant now = Instant.now();
        addQty.forEach((sym, qty) -> {
            double addAvg = addCost.get(sym) / qty;
            PortfolioStock ps = bySym.get(sym);
            if (ps != null) {
                int newQty = ps.getQuantity() + qty;
                double newAvg = (ps.getAveragePrice() * ps.getQuantity() + addAvg * qty) / newQty;
                ps.setQuantity(newQty);
                ps.setAveragePrice(newAvg);
                ps.setLastUpdated(now);
            } else {
                PortfolioStock created = PortfolioStock.builder()
                        .stockSymbol(sym)
                        .quantity(qty)
                        .averagePrice(addAvg)
                        .lastUpdated(now)
                        .portfolio(p)
                        .build();

                p.getStocks().add(created);
            }
        });

        p.setTotalValue(p.getStocks().stream()
                .mapToDouble(s -> s.getAveragePrice() * s.getQuantity()).sum()); //recompute to avoid drift
        p.setLastUpdated(now);

        portfolioRepository.save(p);
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
