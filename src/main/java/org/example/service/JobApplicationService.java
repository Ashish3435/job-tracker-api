package org.example.service;

import org.example.entity.JobApplication;
import org.example.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.example.dto.JobApplicationDTO;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    public List<JobApplication> getAllJobs() {
        return jobApplicationRepository.findAll();
    }

    public JobApplication getJobById(Long id) {
        return jobApplicationRepository.findById(id).orElse(null);
    }

    public JobApplication createJob(JobApplication job) {
        return jobApplicationRepository.save(job);
    }

    public void deleteJob(Long id) {
        jobApplicationRepository.deleteById(id);
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
}