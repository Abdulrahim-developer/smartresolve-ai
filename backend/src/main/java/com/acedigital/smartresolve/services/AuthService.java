package com.acedigital.smartresolve.services;

import com.acedigital.smartresolve.dto.AuthResponse;
import com.acedigital.smartresolve.dto.LoginRequest;
import com.acedigital.smartresolve.dto.RegisterRequest;
import com.acedigital.smartresolve.models.Department;
import com.acedigital.smartresolve.models.Role;
import com.acedigital.smartresolve.models.User;
import com.acedigital.smartresolve.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // 1. Safe, Encrypted User Account Registration Pipeline
    public AuthResponse register(RegisterRequest request) {
        // Enforce strict duplicate database constraints
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Account registration blocked: Email profile already exists.");
        }

        // Map raw incoming strings cleanly to strict backend Type-Safe Enums
        Role assignedRole = Role.valueOf(request.getRole().toUpperCase());
        Department assignedDept = null;
        if (request.getDepartment() != null && !request.getDepartment().trim().isEmpty()) {
            assignedDept = Department.valueOf(request.getDepartment().toUpperCase());
        }

        // Convert the plaintext password input into an immutable adaptive BCrypt cryptographic hash block
        String securePasswordHash = passwordEncoder.encode(request.getPassword());

        User freshUser = User.builder()
                .displayName(request.getDisplayName())
                .email(request.getEmail())
                .passwordHash(securePasswordHash)
                .role(assignedRole)
                .department(assignedDept)
                .build();

        // Write data down into your persistent local / AWS RDS PostgreSQL storage engine
        User savedUser = userRepository.save(freshUser);

        // Sign a role-mapped stateless JWT session token key string
        String sessionToken = jwtService.generateToken(savedUser.getEmail(), savedUser.getRole().name());

        return AuthResponse.builder()
                .token(sessionToken)
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .displayName(savedUser.getDisplayName())
                .build();
    }

    // 2. Cryptographic Validation Login Check Pipeline
    public AuthResponse login(LoginRequest request) {
        User userInstance = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Authentication failed: Invalid login parameters."));

        // Match the raw password input text directly against the encrypted BCrypt string signature
        if (!passwordEncoder.matches(request.getPassword(), userInstance.getPasswordHash())) {
            throw new IllegalArgumentException("Authentication failed: Invalid login parameters.");
        }

        // Sign a clean validation session token upon success
        String sessionToken = jwtService.generateToken(userInstance.getEmail(), userInstance.getRole().name());

        return AuthResponse.builder()
                .token(sessionToken)
                .email(userInstance.getEmail())
                .role(userInstance.getRole().name())
                .displayName(userInstance.getDisplayName())
                .build();
    }
}
