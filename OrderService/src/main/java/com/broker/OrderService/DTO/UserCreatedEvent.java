package com.broker.OrderService.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {
    private Long userId;
    private String email;
    private String username;
}
