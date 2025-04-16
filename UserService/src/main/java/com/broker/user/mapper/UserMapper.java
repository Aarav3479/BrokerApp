package com.broker.user.mapper;

import com.broker.user.DTO.RegistrationDTO.RegisterRequest;
import com.broker.user.DTO.UserProfileDTO.UserResponse;
import com.broker.user.Entities.User;

public class UserMapper {
    public static User toEntity(RegisterRequest request, String encodedPassword) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
//        user.setVerified(false);
        return user;
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getUserId(), user.getUsername(), user.getEmail());
    }
}
