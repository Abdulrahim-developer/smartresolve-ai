package com.acedigital.smartresolve.services;

import com.acedigital.smartresolve.dto.GrievanceRequest;
import com.acedigital.smartresolve.dto.GrievanceResponse;
import com.acedigital.smartresolve.models.*;
import com.acedigital.smartresolve.repositories.GrievanceRepository;
import com.acedigital.smartresolve.repositories.UserRepository;
import com.acedigital.smartresolve.services.ai.AiModelEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// ◄ 1. ADD THESE DYNAMIC SPRING CACHE IMPORTS
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
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

    @Transactional
    @CacheEvict(value = "grievances", allEntries = true) // ◄ EVICTS STALE DATA ON NEW SUBMISSIONS
    public GrievanceResponse submitGrievance(GrievanceRequest request, String reporterEmail) {
        log.info("BUSINESS LOGIC: Initiating secure ticket processing loop for reporter: {}", reporterEmail);

        User reporterInstance = userRepository.findByEmail(reporterEmail)
                .orElseThrow(() -> new IllegalArgumentException("Processing blocked: Authenticated user profile not found."));

        Department predictedDepartment = aiModelEngine.predict(request.getDescription());
        Status finalStatus = Status.OPEN;

        if (predictedDepartment == null) {
            log.info("BUSINESS LOGIC: Low-confidence classification detected. Flagging ticket for Human Review.");
            finalStatus = Status.PENDING_HUMAN_REVIEW;
        }

        Grievance freshGrievance = Grievance.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(finalStatus)
                .assignedDepartment(predictedDepartment)
                .reporter(reporterInstance)
                .build();

        Grievance savedGrievance = grievanceRepository.save(freshGrievance);
        return convertToResponseDto(savedGrievance);
    }

    @Transactional(readOnly = true)
    // ◄ INTERCEPTS STUDENT SCRIPTS: Reads from Valkey RAM if exists, otherwise scans PostgreSQL disk
    @Cacheable(value = "grievances", key = "'student:' + #studentEmail")
    public List<GrievanceResponse> getStudentTickets(String studentEmail) {
        log.info("CACHE TRACE: Valkey cache miss. Scanning PostgreSQL hard disk fields for student: {}", studentEmail);
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Query failed: Profile matching email not found."));

        return grievanceRepository.findByReporterId(student.getId()).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "grievances", key = "'all_tickets'") // ◄ CACHES THE UNIVERSAL QUEUE LEDGER
    public List<GrievanceResponse> getAllTickets() {
        log.info("CACHE TRACE: Valkey cache miss. Compiling universal system queue records from database disk.");
        return grievanceRepository.findAllByOrderByIdDesc().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "grievances", allEntries = true) // ◄ EVICTS RAM ON DEPT MODIFICATIONS
    public GrievanceResponse updateTicketDepartment(Long id, String newDepartmentStr) {
        log.info("BUSINESS LOGIC: Attempting structural category correction for reference #SR-00{}", id);

        Grievance grievance = grievanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Query failure: Grievance target record not found for reference code: " + id));

        Department targetedUnit = Department.valueOf(newDepartmentStr.toUpperCase());
        grievance.setAssignedDepartment(targetedUnit);

        if (grievance.getStatus() == Status.PENDING_HUMAN_REVIEW) {
            grievance.setStatus(Status.OPEN);
        }

        Grievance savedGrievance = grievanceRepository.save(grievance);
        return convertToResponseDto(savedGrievance);
    }

    @Transactional
    @CacheEvict(value = "grievances", allEntries = true) // ◄ EVICTS RAM ON STATUS TRANSITIONS
    public GrievanceResponse updateTicketStatus(Long id, String newStatusStr) {
        log.info("BUSINESS LOGIC: Altering state assignment parameter for reference #SR-00{}", id);

        Grievance grievance = grievanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Query failure: Grievance target record not found for reference code: " + id));

        Status targetedStatus = Status.valueOf(newStatusStr.toUpperCase());
        grievance.setStatus(targetedStatus);

        if (targetedStatus == Status.RESOLVED) {
            grievance.setResolvedAt(LocalDateTime.now());
        } else {
            grievance.setResolvedAt(null);
        }

        Grievance savedGrievance = grievanceRepository.save(grievance);
        return convertToResponseDto(savedGrievance);
    }

    private GrievanceResponse convertToResponseDto(Grievance grievance) {
        DateTimeFormatter professionalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

        return GrievanceResponse.builder()
                .id(grievance.getId())
                .title(grievance.getTitle())
                .description(grievance.getDescription())
                .status(grievance.getStatus().name())
                .assignedDepartment(grievance.getAssignedDepartment() != null ? grievance.getAssignedDepartment().name() : "UNASSIGNED")
                .reporterEmail(grievance.getReporter().getEmail())
                .createdAt(grievance.getCreatedAt() != null ? grievance.getCreatedAt().format(professionalFormatter) : null)
                .resolvedAt(grievance.getResolvedAt() != null ? grievance.getResolvedAt().format(professionalFormatter) : null)
                .build();
    }
}
