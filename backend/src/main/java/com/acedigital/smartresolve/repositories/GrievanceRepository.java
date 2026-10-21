package com.acedigital.smartresolve.repositories;

import com.acedigital.smartresolve.models.Grievance;
import com.acedigital.smartresolve.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GrievanceRepository extends JpaRepository<Grievance, Long> {

    // Automatically compiles secure parameterized queries to fetch rows based on reporter identity
    List<Grievance> findByReporterId(Long reporterId);

    List<Grievance> findByAssignedDepartment(Department department);

    List<Grievance> findAllByOrderByIdDesc();
}
