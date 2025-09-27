package com.broker.portfolio.repository;

import com.broker.portfolio.Entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {
    @Query("SELECT p FROM Portfolio p JOIN FETCH p.stocks WHERE p.portfolioId = :id") //FETCH is used to override lazy loading
    Optional<Portfolio> findByIdWithStocks(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Portfolio p LEFT JOIN FETCH p.stocks")
    Set<Portfolio> findAllWithStocks();
    Optional<Portfolio> findByEmail(String email);
    void deleteByEmail(String email);

}
