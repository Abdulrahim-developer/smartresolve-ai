package com.acedigital.smartresolve.controllers;

import com.acedigital.smartresolve.dto.AuthResponse;
import com.acedigital.smartresolve.dto.LoginRequest;
import com.acedigital.smartresolve.dto.RegisterRequest;
import com.acedigital.smartresolve.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Open public routing portal for profile registrations
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // Open public routing portal for account sign-ins
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
