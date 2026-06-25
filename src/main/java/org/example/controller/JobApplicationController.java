package org.example.controller;

import org.example.entity.JobApplication;
import org.example.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.example.service.JobApplicationService;
import org.springframework.data.domain.Sort;

import org.springframework.security.access.prepost.PreAuthorize;
import org.example.dto.JobApplicationDTO;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/api/jobs")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @PostMapping
    public JobApplication createJob(@Valid @RequestBody JobApplication job) {
        return jobApplicationService.createJob(job);
    }

    @GetMapping
    public List<JobApplication> getAllJobs() {
        return jobApplicationService.getAllJobs();
    }

    @GetMapping("/{id}")
    public JobApplication getJobById(@PathVariable Long id) {
        return jobApplicationService.getJobById(id);
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

    @GetMapping("/company/{companyName}")
    public List<JobApplication> getJobsByCompany(
            @PathVariable String companyName) {

        return jobApplicationRepository.findByCompanyName(companyName);
    }

    @GetMapping("/status/{status}")
    public List<JobApplication> getJobsByStatus(
            @PathVariable String status) {

        return jobApplicationRepository.findByStatus(status);
    }

    @GetMapping("/my-jobs")
    public List<JobApplication> getMyJobs() {
        return jobApplicationService.getMyJobs();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Long id) {
        jobApplicationService.deleteJob(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public JobApplication updateJob(@PathVariable Long id,
                                    @RequestBody JobApplication updatedJob) {

        JobApplication existingJob =
                jobApplicationRepository.findById(id).orElse(null);

        if (existingJob == null) {
            return null;
        }

        existingJob.setCompanyName(updatedJob.getCompanyName());
        existingJob.setJobRole(updatedJob.getJobRole());
        existingJob.setStatus(updatedJob.getStatus());
        existingJob.setAppliedDate(updatedJob.getAppliedDate());
        existingJob.setNotes(updatedJob.getNotes());

        return jobApplicationRepository.save(existingJob);
    }
    @GetMapping("/page")
    public Page<JobApplication> getJobsPage(
            @RequestParam int page,
            @RequestParam int size) {

        return jobApplicationRepository.findAll(
                PageRequest.of(page, size));
    }
    @GetMapping("/sort/company")
    public List<JobApplication> sortByCompany() {

        return jobApplicationRepository.findAll(
                Sort.by("companyName"));
    }
    @GetMapping("/sort/status")
    public List<JobApplication> sortByStatus() {

        return jobApplicationRepository.findAll(
                Sort.by("status"));
    }


    @GetMapping("/dto")
    public List<JobApplicationDTO> getAllJobsDTO() {
        return jobApplicationService.getAllJobsDTO();
    }

    @GetMapping("/dto/{id}")
    public JobApplicationDTO getJobDTOById(
            @PathVariable Long id) {

        return jobApplicationService.getJobDTOById(id);
    }
    @GetMapping("/my-dashboard")
    public Map<String, Long> getMyDashboard() {
        return jobApplicationService.getMyDashboardStats();
    }
}