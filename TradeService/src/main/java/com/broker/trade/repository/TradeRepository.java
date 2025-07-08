package com.broker.trade.repository;

import com.broker.trade.Entity.trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<trade, Long> {}