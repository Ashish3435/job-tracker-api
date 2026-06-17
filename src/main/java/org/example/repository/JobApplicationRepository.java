package org.example.repository;


import org.example.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    long countByStatus(String status);

    List<JobApplication> findByCompanyName(String companyName);
    List<JobApplication> findByStatus(String status);
}