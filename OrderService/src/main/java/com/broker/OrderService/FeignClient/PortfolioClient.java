package com.broker.OrderService.FeignClient;

import com.broker.OrderService.DTO.PortfolioResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "portfolio-service", url = "http://portfolio-container:8084")
public interface PortfolioClient {

    @GetMapping("/api/portfolio/byEmail/{email}")
    PortfolioResponse getPortfolioWithEmail(@PathVariable String email);

}
