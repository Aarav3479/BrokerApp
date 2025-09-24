package com.broker.portfolio.repository;

import com.broker.portfolio.Entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {
    @Query("SELECT p FROM Portfolio p JOIN FETCH p.stocks WHERE p.portfolioId = :id") //FETCH is used to override lazy loading
    Optional<Portfolio> findByIdWithStocks(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Portfolio p LEFT JOIN FETCH p.stocks")
    List<Portfolio> findAllWithStocks();
}
