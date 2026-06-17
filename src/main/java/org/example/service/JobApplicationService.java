package org.example.service;

import org.example.entity.JobApplication;
import org.example.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}