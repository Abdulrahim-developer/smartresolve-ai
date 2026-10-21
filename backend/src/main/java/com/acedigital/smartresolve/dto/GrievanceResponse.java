package com.acedigital.smartresolve.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrievanceResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String assignedDepartment;
    private String reporterEmail;
    private String createdAt;
    private String resolvedAt;
}
