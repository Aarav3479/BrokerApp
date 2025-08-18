package com.broker.trade.repository;

import com.broker.trade.Entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {}
