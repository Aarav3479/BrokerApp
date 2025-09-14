package com.broker.trade.repository;

import com.broker.trade.Entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    @Query(value = "SELECT nextval('trade_id_seq')", nativeQuery = true)
    long getNewTradeId();
}
