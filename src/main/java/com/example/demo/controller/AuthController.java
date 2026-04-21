package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RefreshRequest;
import com.example.demo.security.JwtService;
import com.example.demo.service.AuthService;
import com.example.demo.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtService jwtService;

    public AuthController(AuthService authService,
                          TokenBlacklistService tokenBlacklistService,
                          JwtService jwtService) {
        this.authService = authService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/register")
    public String registerInfo() {
        return "Use POST method with JSON payload to register.";
    }

/*
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request.getEmail(), request.getPassword());
    }
*/
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/refresh")
    public String refresh(@RequestBody RefreshRequest request) {

        if (tokenBlacklistService.isBlacklisted(request.getRefreshToken())) {
            throw new RuntimeException("Refresh token is blacklisted");
        }

        String email = jwtService.extractEmail(request.getRefreshToken());

        if (jwtService.isTokenValid(request.getRefreshToken(), email)) {
            return jwtService.generateToken(email);
        }

        throw new RuntimeException("Invalid refresh token");
    }
/*
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
        }

        return "Logged out successfully";
    }
*/

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, @RequestBody RefreshRequest refreshRequest) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            long expiration = jwtService.getRemainingExpiration(token);

            tokenBlacklistService.blacklistToken(token, expiration);
        }

        // Blacklist the refresh token as well
        if (refreshRequest != null && refreshRequest.getRefreshToken() != null) {
            long refreshExpiration = jwtService.getRemainingExpiration(refreshRequest.getRefreshToken());
            tokenBlacklistService.blacklistToken(refreshRequest.getRefreshToken(), refreshExpiration);
        }

        return "Logged out successfully";
    }


}
