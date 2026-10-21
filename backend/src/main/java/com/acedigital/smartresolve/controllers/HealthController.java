package com.acedigital.smartresolve.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j // Injects an efficient SLF4J logger instance automatically via Lombok
public class HealthController {

    private final JdbcTemplate jdbcTemplate;
    private final StringRedisTemplate redisTemplate;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        boolean isDbHealthy = false;
        boolean isCacheHealthy = false;

        // 1. Safe, Lightweight PostgreSQL Integrity Check
        try {
            jdbcTemplate.setQueryTimeout(3); // Hard 3-second execution timeout guard
            Integer dbVerificationResult = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            isDbHealthy = (dbVerificationResult != null && dbVerificationResult == 1);
        } catch (DataAccessException dae) {
            log.error("HEALTH CHECK FAILURE: Relational Database Storage Engine is unreachable: {}", dae.getMessage());
        }

        // 2. Safe, Protocol-Compliant Valkey Cache Check
        try {
            String cacheVerificationResponse = Objects.requireNonNull(redisTemplate.getConnectionFactory())
                    .getConnection()
                    .ping();
            isCacheHealthy = "PONG".equalsIgnoreCase(cacheVerificationResponse);
        } catch (RedisConnectionFailureException rcfe) {
            log.error("HEALTH CHECK FAILURE: Valkey Cache Memory Cluster is unreachable: {}", rcfe.getMessage());
        } catch (Exception e) {
            log.error("HEALTH CHECK FAILURE: Unexpected anomaly occurred during Cache execution: {}", e.getMessage());
        }

        // 3. Assemble the JSON Response Matrix
        String systemOverallStatus = (isDbHealthy && isCacheHealthy) ? "UP" : "DOWN";

        Map<String, Object> responsePayload = Map.of(
                "status", systemOverallStatus,
                "database_postgresql", isDbHealthy ? "CONNECTED" : "DISCONNECTED",
                "cache_valkey", isCacheHealthy ? "CONNECTED" : "DISCONNECTED",
                "timestamp", System.currentTimeMillis()
        );

        // 4. Return Context-Appropriate HTTP Status Codes
        if ("UP".equals(systemOverallStatus)) {
            return ResponseEntity.ok(responsePayload);
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responsePayload);
        }
    }
}
