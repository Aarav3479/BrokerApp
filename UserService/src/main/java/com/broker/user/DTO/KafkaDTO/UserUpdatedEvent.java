package com.broker.user.DTO.KafkaDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdatedEvent {
    private Long userId;
    private String email;
    private String username;
}
