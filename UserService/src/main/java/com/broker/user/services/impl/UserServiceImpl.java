package com.broker.user.services.impl;

import com.broker.user.DTO.RegistrationDTO.AuthResponse;
import com.broker.user.DTO.RegistrationDTO.LoginRequest;
import com.broker.user.DTO.RegistrationDTO.RegisterRequest;
import com.broker.user.DTO.UserProfileDTO.UpdateUserRequest;
import com.broker.user.DTO.UserProfileDTO.UserResponse;
import com.broker.user.DTO.KafkaDTO.UserCreatedEvent;
import com.broker.user.Entities.User;
import com.broker.user.mapper.UserMapper;
import com.broker.user.repository.UserRepository;
import com.broker.user.services.UserService;
import com.broker.user.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public UserResponse register(RegisterRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = UserMapper.toEntity(request, encodedPassword);
        User savedUser = userRepository.save(user);

        UserCreatedEvent event = new UserCreatedEvent(
                savedUser.getUserId(),
                savedUser.getEmail(),
                savedUser.getUsername()
        );


        kafkaTemplate.send("user-created", event);
        kafkaTemplate.send("user-created-portfolio", event);



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
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
        String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return new AuthResponse(jwt,"bearer");
    }
    @Override
    public UserResponse updateUser(String email, UpdateUserRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getNewPassword() != null) {
            String encoded = passwordEncoder.encode(request.getNewPassword());
            user.setPassword(encoded);
        }

        User updated = userRepository.save(user);
        return UserMapper.toResponse(updated);
    }

    @Override
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.deleteById(user.getUserId());
    }
}
