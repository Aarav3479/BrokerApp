package com.broker.portfolio.Mapper;

import com.broker.portfolio.DTO.PortfolioResponse;
import com.broker.portfolio.DTO.StockResponse;
import com.broker.portfolio.Entity.Portfolio;
import com.broker.portfolio.Entity.PortfolioStock;

import java.util.ArrayList;
import java.util.List;

public class EntityMapper {

    public static StockResponse EntityToStockResponse(PortfolioStock stock, Long portfolioId){
        return new StockResponse(stock.getStockId(), stock.getStockSymbol(), stock.getQuantity(),
                stock.getAveragePrice(), portfolioId);
    }

    public static PortfolioResponse EntityToPortfolioResponse(Portfolio portfolio){
        List<StockResponse> stockList = new ArrayList<>();
        for(PortfolioStock stock : portfolio.getStocks()){
            stockList.add(EntityToStockResponse(stock,portfolio.getPortfolioId()));
        }
        return new PortfolioResponse(portfolio.getPortfolioId(), portfolio.getEmail(), portfolio.getTotalValue(), portfolio.getLastUpdated() ,stockList);
    }
}
