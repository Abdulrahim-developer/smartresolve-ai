package com.acedigital.smartresolve;

import com.acedigital.smartresolve.models.Department;
import com.acedigital.smartresolve.services.JwtService;
import com.acedigital.smartresolve.services.ai.AiModelEngine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SmartresolveApplicationTests {

    @Autowired
    private AiModelEngine aiModelEngine;

    @Autowired
    private JwtService jwtService;

    /**
     * TEST 1: Verifies the core Multinomial Naive Bayes text classification baseline layers.
     */
    @Test
    void verifyAiEngineBaselineRouting() {
        String networkIssue = "The hostel broadband internet connection drops packages and WiFi routing is down";
        String maintenanceIssue = "Water is leaking from the ceiling pipes in the chemistry laboratory room";

        Department predictedNetwork = aiModelEngine.predict(networkIssue);
        Department predictedMaintenance = aiModelEngine.predict(maintenanceIssue);

        assertEquals(Department.IT_SUPPORT, predictedNetwork, "AI failed to route network issue to IT Support.");
        assertEquals(Department.FACILITIES, predictedMaintenance, "AI failed to route pipe leak to Facilities.");
    }

    /**
     * TEST 2: Validates the stateless JWT cryptographic signature generation and encoding layers.
     */
    @Test
    void verifyJwtSecurityTokenLifecycle() {
        String testEmail = "Sample1@gmail.com";
        String testRole = "REPORTER";

        // Pass exactly two clean string inputs matching your method signatures perfectly!
        String generatedToken = jwtService.generateToken(testEmail, testRole);

        assertNotNull(generatedToken, "JWT token string returned null payload.");
        assertTrue(generatedToken.startsWith("eyJ"), "Token format string is missing valid Base64 URL headers.");

        // Extract the subject parameter to confirm correct decoding loops
        String extractedEmail = jwtService.extractEmail(generatedToken);
        assertEquals(testEmail, extractedEmail, "Extracted JWT token subject email does not match seeded identity.");
    }
}
