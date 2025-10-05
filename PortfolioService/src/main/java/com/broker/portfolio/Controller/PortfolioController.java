package com.broker.portfolio.Controller;

import com.broker.portfolio.DTO.PortfolioResponse;
import com.broker.portfolio.Entity.PortfolioStock;
import com.broker.portfolio.repository.PortfolioRepository;
import com.broker.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/byId/{portfolioId}")
    public ResponseEntity<PortfolioResponse> getPortfolio(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(portfolioService.getPortfolio(portfolioId));
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<PortfolioResponse> getPortfolioWithEmail(@PathVariable String email){
        return ResponseEntity.ok(portfolioService.getPortfolioWithEmail(email));
    }

    @GetMapping
    public ResponseEntity<Set<PortfolioResponse>> getAllPortfolios() {
        return ResponseEntity.ok(portfolioService.getAllPortfolios());
    }

    @DeleteMapping("/byId/{portfolioId}")
    public ResponseEntity<String> deletePortfolioById(@PathVariable Long portfolioId) {
        portfolioService.deletePortfolioById(portfolioId);
        return ResponseEntity.ok("Portfolio with ID " + portfolioId + " deleted successfully.");
    }
    @DeleteMapping("/byEmail/{email}")
    public ResponseEntity<String> deletePortfolioByEmail(@PathVariable String email) {
        portfolioService.deletePortfolioByEmail(email);
        return ResponseEntity.ok("Portfolio with Email " + email + " deleted successfully.");
    }
    @DeleteMapping
    public ResponseEntity<String> deleteAllPortfolios(){
        portfolioService.deleteAllPortfolios();
        return ResponseEntity.ok("All Portfolios deleted successfully");
    }
    @PostMapping("/{email}")
    public ResponseEntity<String> AddStocksToPortfolio(@PathVariable String email, @RequestBody List<PortfolioStock> newStocksList){
        portfolioService.addStocksToPortfolio(email,newStocksList);
        return ResponseEntity.ok("Stocks successfully added to the portfolio");
    }
}
