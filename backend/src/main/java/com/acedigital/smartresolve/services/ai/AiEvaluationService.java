package com.acedigital.smartresolve.services.ai;

import com.acedigital.smartresolve.models.Department;
import com.acedigital.smartresolve.models.Grievance;
import com.acedigital.smartresolve.models.GrievanceSample;
import com.acedigital.smartresolve.repositories.GrievanceRepository;
import com.acedigital.smartresolve.repositories.GrievanceSampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiEvaluationService {

    private final GrievanceRepository grievanceRepository;
    private final GrievanceSampleRepository grievanceSampleRepository;
    private final AiModelEngine aiModelEngine;

    /**
     * DYNAMICALLY COMPUTES SYSTEM ACCURACY & THE 3X3 CONFUSION MATRIX.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> evaluateModelPerformance() {
        log.info("BUSINESS LOGIC: Initiating dynamic 3x3 evaluation computation over live test rows.");

        List<GrievanceSample> testSamples = grievanceSampleRepository.findByTestSampleTrue();
        if (testSamples.isEmpty()) {
            throw new IllegalStateException("Evaluation blocked: Labeled test dataset partition is empty.");
        }

        List<String> labels = List.of("IT_SUPPORT", "FACILITIES", "ADMINISTRATION");
        int[][] confusionMatrix = new int[3][3];

        int correctPredictionsCount = 0;
        int totalPredictionsCount = testSamples.size();

        for (GrievanceSample sample : testSamples) {
            Department actualDepartment = sample.getDepartment();
            Department predictedDepartment = aiModelEngine.predict(sample.getDescription());

            if (predictedDepartment == null) {
                predictedDepartment = Department.ADMINISTRATION;
            }

            int actualIndex = labels.indexOf(actualDepartment.name());
            int predictedIndex = labels.indexOf(predictedDepartment.name());

            if (actualIndex != -1 && predictedIndex != -1) {
                confusionMatrix[actualIndex][predictedIndex]++;
                if (actualIndex == predictedIndex) {
                    correctPredictionsCount++;
                }
            }
        }

        double computedLiveAccuracy = (double) correctPredictionsCount / totalPredictionsCount;

        List<List<Integer>> matrixPayloadList = List.of(
                List.of(confusionMatrix[0][0], confusionMatrix[0][1], confusionMatrix[0][2]),
                List.of(confusionMatrix[1][0], confusionMatrix[1][1], confusionMatrix[1][2]),
                List.of(confusionMatrix[2][0], confusionMatrix[2][1], confusionMatrix[2][2])
        );

        return Map.of(
                "validation_accuracy", computedLiveAccuracy,
                "confusion_matrix", Map.of(
                        "labels", labels,
                        "matrix_data", matrixPayloadList
                )
        );
    }

    /**
     * SCANS ALL LIVE USER TICKETS TO DYNAMICALLY OPTIMIZE THE AI PARAMS.
     * Captures any ticket with an assigned department to adapt model weights instantly!
     */
    @Transactional
    public Map<String, Object> triggerSystemRetraining() {
        log.info("BUSINESS LOGIC: Scanning PostgreSQL data clusters for human-verified training pairs.");

        List<Grievance> allGrievances = grievanceRepository.findAll();
        int dynamicTokensProcessedCount = 0;

        for (Grievance ticket : allGrievances) {
            // FIXED LOGIC: Train on ANY ticket that has been actively routed to a department, open or resolved!
            if (ticket.getAssignedDepartment() != null) {

                // FIXED TYPE MATCH: Append .name() to pass a clean String literal to the model engine
                aiModelEngine.updateModelMemoryParameters(ticket.getDescription(), ticket.getAssignedDepartment().name());
                dynamicTokensProcessedCount++;
            }
        }

        log.info("BUSINESS LOGIC: Successfully processed {} dynamic text feature arrays.", dynamicTokensProcessedCount);

        Map<String, Object> summaryReport = new HashMap<>();
        summaryReport.put("status", "SUCCESS");
        summaryReport.put("dynamic_records_ingested", dynamicTokensProcessedCount);
        summaryReport.put("timestamp", java.time.LocalDateTime.now().toString());

        return summaryReport;
    }
}
