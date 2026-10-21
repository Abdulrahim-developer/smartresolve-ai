package com.acedigital.smartresolve.controllers;

import com.acedigital.smartresolve.services.ai.AiEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@Slf4j // Injects our high-performance log monitoring features cleanly
public class AiEvaluationController {

    private final AiEvaluationService evaluationService;

    /**
     * Exposes the complete Multinomial Naive Bayes performance report dashboard.
     */
    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getModelPerformanceMetrics() {
        try {
            Map<String, Object> performanceMetricsPayload = evaluationService.evaluateModelPerformance();
            return ResponseEntity.ok(performanceMetricsPayload);
        } catch (IllegalStateException ise) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ise.getMessage()));
        }
    }

    /**
     * NEW OPERATIONAL CORE: Triggers an isolated in-memory retraining iteration loop.
     * Extracts human-corrected department assignment rows to refine token weights dynamically.
     */
    @PostMapping("/retrain")
    public ResponseEntity<Map<String, Object>> executeModelRetrainingLoop() {
        log.info("REST ENDPOINT: AI engine optimization sequence triggered by administrator.");

        try {
            // Invokes your evaluation service layer to re-compute Laplace probability arrays
            Map<String, Object> retrainSummary = evaluationService.triggerSystemRetraining();

            log.info("REST ENDPOINT: AI engine successfully optimized over live training instances.");
            return ResponseEntity.ok(retrainSummary);
        } catch (Exception e) {
            log.error("REST ENDPOINT: AI optimization loop hit a fatal calculation failure: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Model retraining pipeline dropped: " + e.getMessage()));
        }
    }
}
