package com.example.demo.controller;

import com.example.demo.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {

    private final JwtService jwtService;

    public TestController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private String getTokenInfo(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "No valid token";
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        long remainingMillis = jwtService.getRemainingExpiration(token);

        // Convert milliseconds to minutes and seconds
        long totalSeconds = remainingMillis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format("User: %s | Session expires in %d minutes %d seconds", email, minutes, seconds);
    }

    @GetMapping("/hello")
    public String hello(HttpServletRequest request) {
        return "Hello, secured endpoint! | " + getTokenInfo(request);
    }

    @GetMapping("/user/hello")
    public String userHello(HttpServletRequest request) {
        return "Hello USER | " + getTokenInfo(request);
    }

    @GetMapping("/admin/hello")
    public String adminHello(HttpServletRequest request) {
        return "Hello ADMIN | " + getTokenInfo(request);
    }

}