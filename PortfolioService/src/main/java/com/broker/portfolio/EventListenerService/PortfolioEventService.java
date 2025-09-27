package com.broker.portfolio.EventListenerService;

import com.broker.portfolio.DTO.UserCreatedEvent;
import com.broker.portfolio.DTO.UserDeletedEvent;
import com.broker.portfolio.Entity.Portfolio;
import com.broker.portfolio.repository.PortfolioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;

@Slf4j
@Component
public class PortfolioEventService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public void createNewPortfolio(UserCreatedEvent event){

        if(portfolioRepository.findByEmail(event.getEmail()).isPresent()){
            throw new RuntimeException("Portfolio with the current email already exists");
        }
        Portfolio portfolio = Portfolio.builder()
                .totalValue(0.0)
                .email(event.getEmail())
                .lastUpdated(Instant.now())
                .stocks(new HashSet<>())
                .build();

        portfolioRepository.save(portfolio);
        log.info("New Portfolio Created");
    }

    public void deletePortfolio(UserDeletedEvent event){

        if(portfolioRepository.findByEmail(event.getEmail()).isEmpty()){
            throw new RuntimeException("Portfolio with the current email doesn't exists");
        }

        portfolioRepository.deleteByEmail(event.getEmail());
        log.info("Portfolio with email: {} deleted successfully", event.getEmail());
    }
}
