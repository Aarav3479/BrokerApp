package com.broker.OrderService.FeignClient;

import com.broker.OrderService.DTO.PortfolioResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "portfolio-service", url = "http://localhost:8084/api/portfolio/byEmail")
public interface PortfolioClient {

    @GetMapping("/{email}")
    PortfolioResponse getPortfolioWithEmail(@PathVariable String email);
}
