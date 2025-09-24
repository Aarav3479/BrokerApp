package com.broker.portfolio.Controller;

import com.broker.portfolio.DTO.PortfolioResponse;
import com.broker.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    @Autowired
    private final PortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<PortfolioResponse> getPortfolio(Long portfolioId) {
        return ResponseEntity.ok(portfolioService.getPortfolio(portfolioId));
    }

    @GetMapping
    public ResponseEntity<List<PortfolioResponse>> getAllPortfolios() {
        return ResponseEntity.ok(portfolioService.getAllPortfolios());
    }

    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<String> deletePortfolio(@PathVariable Long portfolioId) {
        portfolioService.deletePortfolio(portfolioId);
        return ResponseEntity.ok("Portfolio with ID " + portfolioId + " deleted successfully.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllPortfolios(){
        portfolioService.deleteAllPortfolios();
        return ResponseEntity.ok("All Portfolios deleted successfully");
    }
}
