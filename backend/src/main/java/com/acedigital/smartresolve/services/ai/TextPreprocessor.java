package com.acedigital.smartresolve.services.ai;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TextPreprocessor {

    // Cleaned English stop words array - negation modifiers are preserved to protect semantic intent
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are",
            "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can",
            "cannot", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from",
            "further", "had", "has", "have", "having", "he", "her", "here", "hers", "herself", "him", "himself",
            "his", "how", "i", "if", "in", "into", "is", "it", "its", "itself", "me", "more", "most", "my", "myself",
            "nor", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out",
            "over", "own", "same", "she", "should", "so", "some", "such", "than", "that", "the", "their", "theirs",
            "them", "themselves", "then", "there", "these", "they", "this", "those", "through", "to", "too",
            "under", "until", "up", "very", "was", "we", "were", "what", "when", "where", "which", "while",
            "who", "whom", "why", "with", "would", "you", "your", "yours", "yourself", "yourselves"
    ));

    /**
     * Cleans, normalizes, and tokenizes raw grievance text while preserving negation context.
     * Example: "The network is not working" -> ["network", "not", "working"]
     */
    public List<String> tokenize(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // Convert to lowercase and strip out punctuation markers
        String normalized = rawText.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s]", " ");

        // Split text across whitespace sequences
        String[] rawTokens = normalized.split("\\s+");

        return Arrays.stream(rawTokens)
                .map(String::trim)
                .filter(token -> !token.isEmpty())
                .filter(token -> !STOP_WORDS.contains(token)) // Drops noise words but keeps negations
                .collect(Collectors.toList());
    }
}
