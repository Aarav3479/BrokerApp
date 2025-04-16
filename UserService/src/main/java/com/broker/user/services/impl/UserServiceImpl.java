package com.broker.user.services.impl;

import com.broker.user.DTO.RegistrationDTO.AuthResponse;
import com.broker.user.DTO.RegistrationDTO.LoginRequest;
import com.broker.user.DTO.RegistrationDTO.RegisterRequest;
import com.broker.user.DTO.UserProfileDTO.UpdateUserRequest;
import com.broker.user.DTO.UserProfileDTO.UserResponse;
import com.broker.user.Entities.User;
import com.broker.user.mapper.UserMapper;
import com.broker.user.repository.UserRepository;
import com.broker.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;


    @Override
    public UserResponse register(RegisterRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = UserMapper.toEntity(request, encodedPassword);
        User savedUser = userRepository.save(user);

        //Send response to kafka

        return UserMapper.toResponse(savedUser);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user->new UserResponse(user.getUserId(),user.getUsername(),user.getEmail()))
                .collect(Collectors.toList());
    }
    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Normally you'd return a JWT token here
        return new AuthResponse("mock-jwt-token-for-" + user.getUsername(),"bearer");
    }
    @Override
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getNewPassword() != null) {
            String encoded = passwordEncoder.encode(request.getNewPassword());
            user.setPassword(encoded);
        }

        User updated = userRepository.save(user);
        return UserMapper.toResponse(updated);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.deleteById(userId);
    }
}
