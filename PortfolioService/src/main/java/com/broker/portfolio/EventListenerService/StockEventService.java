package com.broker.portfolio.EventListenerService;

import com.broker.portfolio.DTO.StockResponse;
import com.broker.portfolio.DTO.TradePlacedEvent;
import com.broker.portfolio.Entity.Portfolio;
import com.broker.portfolio.Entity.PortfolioStock;
import com.broker.portfolio.Mapper.EntityMapper;
import com.broker.portfolio.repository.PortfolioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class StockEventService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public void addTradeToPortfolio(TradePlacedEvent event){
        Optional<Portfolio> portfolio = portfolioRepository.findByEmail(event.getEmail());
        if(portfolio.isEmpty()){
            throw new RuntimeException("Portfolio doesn't exist");
        }
        double totalValueChange = event.getPrice()*event.getQuantity();
        int quantityChange = event.getQuantity();
        if(event.getType() == TradePlacedEvent.OrderType.SELL){
            totalValueChange *= -1.0;
            quantityChange *= -1;
        }
        portfolio.get().setTotalValue(portfolio.get().getTotalValue()+totalValueChange);

        Set<PortfolioStock> stockList = portfolio.get().getStocks();
        PortfolioStock portfolioStock = stockList.stream()
                .filter(s -> s.getStockSymbol().equals(event.getStockSymbol()))
                .findFirst()
                .orElse(null);

        StockResponse stockResponse;
        if(portfolioStock != null){
            int newQuantity = portfolioStock.getQuantity() + quantityChange;
            if(newQuantity == 0){
                stockList.remove(portfolioStock);
            }
            else{
                if (event.getType() == TradePlacedEvent.OrderType.BUY) {
                    double newAvg = (portfolioStock.getAveragePrice() * portfolioStock.getQuantity()
                            + event.getPrice() * event.getQuantity())
                            / newQuantity;
                    portfolioStock.setAveragePrice(newAvg);
                }
                portfolioStock.setQuantity(newQuantity);
                portfolioStock.setLastUpdated(event.getTradeTimestamp());
            }
            stockResponse = EntityMapper.EntityToStockResponse(portfolioStock,portfolio.get().getPortfolioId());
        }
        else{
            PortfolioStock newsStock = PortfolioStock.builder()
                    .stockSymbol(event.getStockSymbol())
                    .quantity(event.getQuantity())
                    .averagePrice(event.getPrice())
                    .lastUpdated(event.getTradeTimestamp())
                    .portfolio(portfolio.get()).build();
            stockList.add(newsStock);
            stockResponse = EntityMapper.EntityToStockResponse(newsStock,portfolio.get().getPortfolioId());
        }

        portfolioRepository.save(portfolio.get());
        log.info("Stock added to Portfolio: {}",stockResponse);
    }
}
