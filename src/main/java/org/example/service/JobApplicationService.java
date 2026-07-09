package org.example.service;

import org.example.entity.JobApplication;
import org.example.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.entity.User;
import org.example.repository.UserRepository;
import java.util.Map;
import java.util.HashMap;
import org.example.dto.JobRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.example.dto.JobApplicationDTO;
import java.util.stream.Collectors;
import org.example.dto.UpdateJobStatusRequest;

import java.util.List;

@Service
public class JobApplicationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    public List<JobApplication> getAllJobs() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        if ("ADMIN".equals(user.getRole())) {
            return jobApplicationRepository.findAll();
        }

        return jobApplicationRepository.findByUser(user);
    }

    public JobApplication getJobById(Long id) {
        return jobApplicationRepository.findById(id).orElse(null);
    }

    public JobApplication createJob(JobRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        JobApplication job = new JobApplication();

        job.setCompanyName(request.getCompanyName());
        job.setJobRole(request.getJobRole());
        job.setStatus(request.getStatus());
        job.setAppliedDate(request.getAppliedDate());
        job.setNotes(request.getNotes());

        job.setUser(user);

        return jobApplicationRepository.save(job);
    }
    public void deleteJob(Long id) {
        jobApplicationRepository.deleteById(id);
    }

    public JobApplication updateStatus(Long id,
                                       UpdateJobStatusRequest request) {

        JobApplication job =
                jobApplicationRepository.findById(id).orElse(null);

        if (job == null) {
            return null;
        }

        job.setStatus(request.getStatus());

        return jobApplicationRepository.save(job);
    }

    public List<JobApplicationDTO> getAllJobsDTO() {

        return jobApplicationRepository.findAll()
                .stream()
                .map(job -> new JobApplicationDTO(
                        job.getCompanyName(),
                        job.getJobRole(),
                        job.getStatus()
                ))
                .collect(Collectors.toList());
    }

    public JobApplicationDTO getJobDTOById(Long id) {

        JobApplication job =
                jobApplicationRepository.findById(id).orElse(null);

        if (job == null) {
            return null;
        }

        return new JobApplicationDTO(
                job.getCompanyName(),
                job.getJobRole(),
                job.getStatus()
        );
    }
    public List<JobApplication> getMyJobs() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        return jobApplicationRepository.findByUser(user);
    }
    public Map<String, Long> getMyDashboardStats() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        Map<String, Long> stats = new HashMap<>();

        stats.put("Applied",
                jobApplicationRepository.countByUserAndStatus(user, "Applied"));

        stats.put("Interview",
                jobApplicationRepository.countByUserAndStatus(user, "Interview"));

        stats.put("Offer",
                jobApplicationRepository.countByUserAndStatus(user, "Offer"));

        stats.put("Rejected",
                jobApplicationRepository.countByUserAndStatus(user, "Rejected"));

        return stats;
    }
}