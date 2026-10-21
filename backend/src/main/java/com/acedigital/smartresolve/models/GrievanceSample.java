package com.acedigital.smartresolve.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grievance_samples")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrievanceSample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Department department;

    @Column(name = "is_test_sample", nullable = false)
    private boolean testSample; // Flag to separate the 30 held-out test rows from training loops
}
