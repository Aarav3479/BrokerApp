package com.broker.user.services;

import com.broker.user.DTO.RegistrationDTO.AuthResponse;
import com.broker.user.DTO.RegistrationDTO.LoginRequest;
import com.broker.user.DTO.RegistrationDTO.RegisterRequest;
import com.broker.user.DTO.UserProfileDTO.UpdateUserRequest;
import com.broker.user.DTO.UserProfileDTO.UserResponse;
import com.broker.user.Entities.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    UserResponse register(RegisterRequest request);
    List<UserResponse> getAllUsers();
    AuthResponse login(LoginRequest request);
    UserResponse updateUser(Long userId, UpdateUserRequest request);
    void deleteUser(Long userId);
}
