package com.acedigital.smartresolve.services.ai;

import com.acedigital.smartresolve.models.Department;
import com.acedigital.smartresolve.models.GrievanceSample;
import com.acedigital.smartresolve.repositories.GrievanceSampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiModelEngine {

    private final GrievanceSampleRepository repository;
    private final TextPreprocessor preprocessor;

    // Log-probability maps representing our trained model parameters in local memory
    private final Map<Department, Double> logPriors = new ConcurrentHashMap<>();
    private final Map<Department, Map<String, Double>> logLikelihoods = new ConcurrentHashMap<>();
    private final Set<String> globalVocabulary = Collections.newSetFromMap(new ConcurrentHashMap<>());

    // High confidence classification log-difference threshold check guard
    private static final double CONFIDENCE_THRESHOLD_DELTA = 0.55;

    /**
     * Executes a distinct single-pass training cycle over the 60 training rows.
     * Isolates test data by strictly querying where is_test_sample = false.
     */
    public synchronized void trainModel() {
        log.info("AI ENGINE: Initiating standard Multinomial Naive Bayes calculation pass...");

        // 1. Fetch only the 60 training data samples (20 per department)
        List<GrievanceSample> trainingSamples = repository.findByTestSampleFalse();
        if (trainingSamples.isEmpty()) {
            log.warn("AI ENGINE: Aborting calculation sequence. Labeled training partition is empty.");
            return;
        }

        // Reset previous memory matrix parameters
        logPriors.clear();
        logLikelihoods.clear();
        globalVocabulary.clear();

        Map<Department, List<String>> tokensByDept = new HashMap<>();
        for (Department dept : Department.values()) {
            tokensByDept.put(dept, new ArrayList<>());
            logLikelihoods.put(dept, new ConcurrentHashMap<>());
        }

        double totalDocs = trainingSamples.size();
        Map<Department, Double> docCountsByDept = new HashMap<>();

        // 2. Tokenize training content and count term occurrences per channel
        for (GrievanceSample sample : trainingSamples) {
            Department dept = sample.getDepartment();
            docCountsByDept.put(dept, docCountsByDept.getOrDefault(dept, 0.0) + 1.0);

            List<String> tokens = preprocessor.tokenize(sample.getDescription());
            tokensByDept.get(dept).addAll(tokens);
            globalVocabulary.addAll(tokens);
        }

        double vocabSize = globalVocabulary.size();

        // 3. Compute mathematically standard Prior and Log-Likelihood parameters via Laplace Smoothing
        for (Department dept : Department.values()) {
            double docsInDept = docCountsByDept.getOrDefault(dept, 0.0);

            // P(C) prior calculation mapping
            logPriors.put(dept, Math.log((docsInDept + 1.0) / (totalDocs + Department.values().length)));

            List<String> deptTokens = tokensByDept.get(dept);
            double totalWordsInDept = deptTokens.size();

            Map<String, Double> tokenCounts = new HashMap<>();
            for (String token : deptTokens) {
                tokenCounts.put(token, tokenCounts.getOrDefault(token, 0.0) + 1.0);
            }

            // P(Word | C) conditional probability loop (Strictly independent of IDF scores)
            for (String word : globalVocabulary) {
                double count = tokenCounts.getOrDefault(word, 0.0);
                double logLikelihood = Math.log((count + 1.0) / (totalWordsInDept + vocabSize));
                logLikelihoods.get(dept).put(word, logLikelihood);
            }
        }
        log.info("AI ENGINE: Model parameters compiled successfully. Vocabulary dictionary size: {}", vocabSize);
    }

    /**
     * Scores incoming text parameters across calculated log maps.
     * Returns null if classification scores are low-confidence, triggering human-review status steps.
     */
    public Department predict(String rawComplaintText) {
        if (rawComplaintText == null || rawComplaintText.trim().isEmpty()) {
            return null; // Empty descriptions route straight to human intervention
        }

        List<String> inputTokens = preprocessor.tokenize(rawComplaintText);
        if (inputTokens.isEmpty()) {
            return null; // If stop-word processing reduces input to zero length, drop to review
        }

        // Lazy initialization guard if memory maps are empty
        if (logPriors.isEmpty()) {
            trainModel();
        }

        Department predictedDept = null;
        double maxScore = Double.NEGATIVE_INFINITY;
        double secondaryScore = Double.NEGATIVE_INFINITY;

        // Sum log-likelihood parameters across operational department vectors
        for (Department dept : Department.values()) {
            double currentScore = logPriors.getOrDefault(dept, 0.0);
            Map<String, Double> deptLikelihoods = logLikelihoods.get(dept);

            for (String token : inputTokens) {
                if (globalVocabulary.contains(token)) {
                    currentScore += deptLikelihoods.getOrDefault(token, 0.0);
                }
                // Out-of-Vocabulary words add a neutral 0 modifier, preserving metric ratios
            }

            // Tracking the top two classification channels to inspect prediction margin delta
            if (currentScore > maxScore) {
                secondaryScore = maxScore;
                maxScore = currentScore;
                predictedDept = dept;
            } else if (currentScore > secondaryScore) {
                secondaryScore = currentScore;
            }
        }

        // Enforce Low-Confidence Protection: If prediction margin is too tight, flag for human verification
        double marginDelta = Math.abs(maxScore - secondaryScore);
        if (marginDelta < CONFIDENCE_THRESHOLD_DELTA) {
            log.info("AI ENGINE: Ambiguous log probability detected (Margin Delta: {}). Forcing Human Review flag.", marginDelta);
            return null;
        }

        return predictedDept;
    }

    /**
     * NEW OPERATIONAL CORE: Appends live text token weights dynamically onto internal probability vectors inside memory.
     * Implements in-memory online adjustment parameters to absorb new verified training tokens seamlessly.
     */
    public synchronized void updateModelMemoryParameters(String text, String departmentLabel) {
        if (text == null || text.trim().isEmpty() || departmentLabel == null) {
            return;
        }

        try {
            // 1. Safely parse the target string category into your type-safe model Enum constraint values
            Department targetDept = Department.valueOf(departmentLabel.toUpperCase());
            List<String> freshTokens = preprocessor.tokenize(text);

            if (freshTokens.isEmpty()) {
                return;
            }

            log.info("AI ENGINE: Processing online learning pass for [{}] targeting text features: {}", targetDept, freshTokens.size());

            // 2. Expand your global vocabulary system set array with the new feature descriptors
            globalVocabulary.addAll(freshTokens);
            double updatedVocabSize = globalVocabulary.size();

            // 3. Access the existing likelihood vectors matrix for this specific department partition
            Map<String, Double> targetDeptLikelihoods = logLikelihoods.get(targetDept);
            if (targetDeptLikelihoods == null) {
                targetDeptLikelihoods = new ConcurrentHashMap<>();
                logLikelihoods.put(targetDept, targetDeptLikelihoods);
            }

            // 4. Update probability distribution scores inside memory matching Laplace smoothing rules
            for (String token : freshTokens) {
                // Approximate historical token count by converting log value back to absolute counts
                double currentLogLikelihood = targetDeptLikelihoods.getOrDefault(token, Math.log(1.0 / (updatedVocabSize + 100)));
                double estimatedPreviousCount = Math.exp(currentLogLikelihood) * (updatedVocabSize + 100);

                // Increment term count weight factor by 1
                double adjustedCount = estimatedPreviousCount + 1.0;
                double newLogLikelihood = Math.log((adjustedCount + 1.0) / (adjustedCount + updatedVocabSize));

                targetDeptLikelihoods.put(token, newLogLikelihood);
            }

        } catch (IllegalArgumentException iae) {
            log.error("AI ENGINE: Online tuning dropped due to invalid department mapping label: {}", departmentLabel);
        }
    }
}
