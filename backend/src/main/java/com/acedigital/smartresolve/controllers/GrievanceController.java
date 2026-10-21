package com.acedigital.smartresolve.controllers;

import com.acedigital.smartresolve.dto.GrievanceRequest;
import com.acedigital.smartresolve.dto.GrievanceResponse;
import com.acedigital.smartresolve.services.GrievanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grievances")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}) // Enables cross-origin dashboard communication
@Slf4j
public class GrievanceController {

    private final GrievanceService grievanceService;

    /**
     * Public portal endpoint enabling authenticated student profiles to file real-time grievances.
     */
    @PostMapping("/submit")
    public ResponseEntity<GrievanceResponse> fileNewGrievance(
            @RequestBody GrievanceRequest request,
            Principal principal
    ) {
        String authenticatedUserEmail = principal.getName();
        log.info("REST ENDPOINT: Received incoming ticket payload from user: {}", authenticatedUserEmail);

        GrievanceResponse processedResponse = grievanceService.submitGrievance(request, authenticatedUserEmail);
        return ResponseEntity.ok(processedResponse);
    }

    /**
     * Secure tracking route allowing individual profiles to load their historical submitted tickets.
     */
    @GetMapping("/my-tickets")
    public ResponseEntity<List<GrievanceResponse>> loadPersonalGrievances(Principal principal) {
        String authenticatedUserEmail = principal.getName();
        log.info("REST ENDPOINT: Fetching ticket dashboard matrix for user: {}", authenticatedUserEmail);

        List<GrievanceResponse> historicalTickets = grievanceService.getStudentTickets(authenticatedUserEmail);
        return ResponseEntity.ok(historicalTickets);
    }

    /**
     * NEW CORE: Loads the complete universal ledger queue for STAFF and ADMIN users.
     */
    @GetMapping("/all-tickets")
    public ResponseEntity<List<GrievanceResponse>> loadAllGrievances() {
        log.info("REST ENDPOINT: Administrative query fetching the global triage queue.");
        List<GrievanceResponse> allTickets = grievanceService.getAllTickets(); // Calls your dynamic service layer
        return ResponseEntity.ok(allTickets);
    }

    /**
     * NEW CORE: Dynamic department re-assignment utility endpoint.
     */
    @PutMapping("/{id}/reassign")
    public ResponseEntity<GrievanceResponse> reassignGrievanceDepartment(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload
    ) {
        String newDepartment = payload.get("department");
        log.info("REST ENDPOINT: Reassigning grievance reference #SR-00{} to unit: {}", id, newDepartment);

        GrievanceResponse updatedTicket = grievanceService.updateTicketDepartment(id, newDepartment);
        return ResponseEntity.ok(updatedTicket);
    }

    /**
     * NEW CORE: Dynamic status update resolution endpoint enabling staff to close tickets.
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<GrievanceResponse> updateGrievanceStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload
    ) {
        String newStatus = payload.get("status");
        log.info("REST ENDPOINT: Transitioning tracking state of reference #SR-00{} to: {}", id, newStatus);

        GrievanceResponse updatedTicket = grievanceService.updateTicketStatus(id, newStatus);
        return ResponseEntity.ok(updatedTicket);
    }
}
