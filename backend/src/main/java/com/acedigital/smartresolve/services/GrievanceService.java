package com.acedigital.smartresolve.services;

import com.acedigital.smartresolve.dto.GrievanceRequest;
import com.acedigital.smartresolve.dto.GrievanceResponse;
import com.acedigital.smartresolve.models.*;
import com.acedigital.smartresolve.repositories.GrievanceRepository;
import com.acedigital.smartresolve.repositories.UserRepository;
import com.acedigital.smartresolve.services.ai.AiModelEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
@Slf4j
public class GrievanceService {

    private final GrievanceRepository grievanceRepository;
    private final UserRepository userRepository;
    private final AiModelEngine aiModelEngine;

    /**
     * Processes and records incoming real-time student grievances.
     * Integrates with the AI Model Engine to calculate category routing dynamically.
     */
    @Transactional
    public GrievanceResponse submitGrievance(GrievanceRequest request, String reporterEmail) {
        log.info("BUSINESS LOGIC: Initiating secure ticket processing loop for reporter: {}", reporterEmail);

        // 1. Fetch the authenticated reporter profile out of our relational user table
        User reporterInstance = userRepository.findByEmail(reporterEmail)
                .orElseThrow(() -> new IllegalArgumentException("Processing blocked: Authenticated user profile not found."));

        // 2. Pass the live description string into our standard Naive Bayes AI engine for prediction
        Department predictedDepartment = aiModelEngine.predict(request.getDescription());

        Status finalStatus = Status.OPEN;

        // 3. Ambiguity Resolution: If the AI margin score falls below confidence thresholds (returns null),
        // flag the ticket status for manual oversight instead of forcing a wrong assignment
        if (predictedDepartment == null) {
            log.info("BUSINESS LOGIC: Low-confidence classification detected. Flagging ticket for Human Review.");
            finalStatus = Status.PENDING_HUMAN_REVIEW;
        }

        // 4. Map parameters cleanly into a relational Grievance instance structure
        Grievance freshGrievance = Grievance.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(finalStatus)
                .assignedDepartment(predictedDepartment)
                .reporter(reporterInstance)
                .build();

        // 5. Execute secure parameterized transaction down into your persistent PostgreSQL database
        Grievance savedGrievance = grievanceRepository.save(freshGrievance);

        // 6. Map the saved record onto our loop-safe DTO before returning it to prevent Jackson crashes
        return convertToResponseDto(savedGrievance);
    }

    /**
     * Fetches all live grievances filed by a specific student profile.
     */
    @Transactional(readOnly = true)
    public List<GrievanceResponse> getStudentTickets(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Query failed: Profile matching email not found."));

        return grievanceRepository.findByReporterId(student.getId()).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * NEW CORE: Fetches every single grievance record in the university, sorted with the newest on top.
     */
    @Transactional(readOnly = true)
    public List<GrievanceResponse> getAllTickets() {
        log.info("BUSINESS LOGIC: Compiling universal queue records ledger array.");
        return grievanceRepository.findAllByOrderByIdDesc().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * NEW CORE: Dynamic department re-assignment utility service method.
     */
    @Transactional
    public GrievanceResponse updateTicketDepartment(Long id, String newDepartmentStr) {
        log.info("BUSINESS LOGIC: Attempting structural category correction for reference #SR-00{}", id);

        Grievance grievance = grievanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Query failure: Grievance target record not found for reference code: " + id));

        // Safely parse the incoming string text into your type-safe model Enum constraint values
        Department targetedUnit = Department.valueOf(newDepartmentStr.toUpperCase());
        grievance.setAssignedDepartment(targetedUnit);

        // If a ticket was previously stuck in manual review status due to low threshold flags,
        // clear its status back to an active OPEN operational tier once department routing is chosen
        if (grievance.getStatus() == Status.PENDING_HUMAN_REVIEW) {
            grievance.setStatus(Status.OPEN);
        }

        Grievance savedGrievance = grievanceRepository.save(grievance);
        return convertToResponseDto(savedGrievance);
    }

    /**
     * NEW CORE: Dynamic status update resolution service method enabling staff to resolve issues.
     */
    @Transactional
    public GrievanceResponse updateTicketStatus(Long id, String newStatusStr) {
        log.info("BUSINESS LOGIC: Altering state assignment parameter for reference #SR-00{}", id);

        Grievance grievance = grievanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Query failure: Grievance target record not found for reference code: " + id));

        Status targetedStatus = Status.valueOf(newStatusStr.toUpperCase());
        grievance.setStatus(targetedStatus);

        // System Automation: If the ticket tracking state transitions to RESOLVED,
        // write the accurate system timestamp marker right inside the resolution history logs
        if (targetedStatus == Status.RESOLVED) {
            grievance.setResolvedAt(LocalDateTime.now());
        } else {
            grievance.setResolvedAt(null); // Clears the timestamp metric if a ticket is re-opened by admin choices
        }

        Grievance savedGrievance = grievanceRepository.save(grievance);
        return convertToResponseDto(savedGrievance);
    }

    /**
     * Loop-Safe mapping conversion utility that isolates internal entities from external JSON engines.
     */
    /**
     * Loop-Safe mapping conversion utility that isolates internal entities from external JSON engines.
     */
    private GrievanceResponse convertToResponseDto(Grievance grievance) {
        // Updated pattern format: 'hh' handles 12-hour time and 'a' appends the AM/PM marker string natively!
        DateTimeFormatter professionalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");


        return GrievanceResponse.builder()
                .id(grievance.getId())
                .title(grievance.getTitle())
                .description(grievance.getDescription())
                .status(grievance.getStatus().name())
                .assignedDepartment(grievance.getAssignedDepartment() != null ? grievance.getAssignedDepartment().name() : "UNASSIGNED")
                .reporterEmail(grievance.getReporter().getEmail())
                // Pass your timestamps through the custom formatter string matrix cleanly!
                .createdAt(grievance.getCreatedAt() != null ? grievance.getCreatedAt().format(professionalFormatter) : null)
                .resolvedAt(grievance.getResolvedAt() != null ? grievance.getResolvedAt().format(professionalFormatter) : null)
                .build();
    }

}
