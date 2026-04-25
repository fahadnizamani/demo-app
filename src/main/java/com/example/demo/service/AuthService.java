package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.kafka.UserEventProducer;
import com.example.demo.kafka.UserRegisteredEvent;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserEventProducer producer;
    private final RefreshTokenRepository tokenRepository;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       UserEventProducer producer,
                       RefreshTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.producer = producer;
        this.tokenRepository = tokenRepository;
    }

    public String register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        UserRegisteredEvent event = new UserRegisteredEvent(
                user.getEmail(),
                user.getUsername(),
                "USER_REGISTERED"
        );
        producer.sendUserRegisteredEvent(event);

        return "User registered successfully";
    }
/*
    public String login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtService.generateToken(user.getEmail());
    }
*/

    public AuthResponse login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtService.generateToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);


        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setToken(refreshToken);
        tokenEntity.setUserEmail(user.getEmail());
        tokenEntity.setDeviceInfo("web");
        tokenEntity.setValid(true);
        tokenEntity.setExpiryDate(LocalDateTime.now().plusDays(7));

        tokenRepository.save(tokenEntity);

        return new AuthResponse(accessToken, refreshToken);
    }

    /*
    public void invalidateRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRefreshToken(null);
        userRepository.save(user);
    }
*/
    public void invalidateAllRefreshTokens(String email) {

        List<RefreshToken> tokens = tokenRepository.findByUserEmailAndValidTrue(email);

        for (RefreshToken t : tokens) {
            t.setValid(false);
        }

        tokenRepository.saveAll(tokens);
    }

    public AuthResponse refreshToken(String refreshToken) {

        RefreshToken token = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (!token.isValid() || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Expired or invalid refresh token");
        }

        User user = userRepository.findByEmail(token.getUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtService.generateToken(user.getEmail());

        return new AuthResponse(newAccessToken, refreshToken);
    }


}
