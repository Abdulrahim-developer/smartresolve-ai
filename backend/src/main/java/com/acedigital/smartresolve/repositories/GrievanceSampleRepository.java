package com.acedigital.smartresolve.repositories;

import com.acedigital.smartresolve.models.GrievanceSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GrievanceSampleRepository extends JpaRepository<GrievanceSample, Long> {

    // Explicitly filters out test data to protect the model from training leakages
    List<GrievanceSample> findByTestSampleFalse();

    // Loads only the 30 held-out evaluation samples for clean diagnostic metric scoring
    List<GrievanceSample> findByTestSampleTrue();
}
