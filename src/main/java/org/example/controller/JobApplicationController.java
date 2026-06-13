package org.example.controller;

import org.example.entity.JobApplication;
import org.example.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobApplicationController {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @PostMapping
    public JobApplication createJob(@Valid @RequestBody JobApplication job) {
        return jobApplicationRepository.save(job);
    }

    @GetMapping
    public List<JobApplication> getAllJobs() {
        return jobApplicationRepository.findAll();
    }
    @GetMapping("/{id}")
    public JobApplication getJobById(@PathVariable Long id) {
        return jobApplicationRepository.findById(id).orElse(null);
    }
    @PutMapping("/{id}/status")
    public JobApplication updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        JobApplication job =
                jobApplicationRepository.findById(id).orElse(null);

        if (job != null) {
            job.setStatus(status);
            return jobApplicationRepository.save(job);
        }

        return null;
    }
    @GetMapping("/dashboard")
    public Map<String, Long> getDashboardStats() {

        Map<String, Long> stats = new HashMap<>();

        stats.put("totalApplications", jobApplicationRepository.count());

        stats.put("applied",
                jobApplicationRepository.countByStatus("Applied"));

        stats.put("interview",
                jobApplicationRepository.countByStatus("Interview"));

        stats.put("offer",
                jobApplicationRepository.countByStatus("Offer"));

        stats.put("rejected",
                jobApplicationRepository.countByStatus("Rejected"));

        return stats;
    }
    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Long id) {
        jobApplicationRepository.deleteById(id);
    }
}