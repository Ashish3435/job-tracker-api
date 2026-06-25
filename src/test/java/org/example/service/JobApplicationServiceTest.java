package org.example.service;

import org.example.entity.JobApplication;
import org.example.repository.JobApplicationRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import org.example.dto.JobApplicationDTO;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.example.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JobApplicationService jobApplicationService;

    @Test
    void shouldReturnJobById() {

        JobApplication job = new JobApplication();
        job.setCompanyName("Google");

        when(jobApplicationRepository.findById(1L))
                .thenReturn(Optional.of(job));

        JobApplication result =
                jobApplicationService.getJobById(1L);

        assertNotNull(result);
        assertEquals("Google", result.getCompanyName());
    }
    @Test
    void shouldDeleteJob() {

        jobApplicationService.deleteJob(1L);

        verify(jobApplicationRepository).deleteById(1L);
    }
    @Test
    void shouldReturnAllJobsDTO() {

        JobApplication job1 = new JobApplication();
        job1.setCompanyName("Google");
        job1.setJobRole("Software Engineer");
        job1.setStatus("Applied");

        JobApplication job2 = new JobApplication();
        job2.setCompanyName("Microsoft");
        job2.setJobRole("Backend Developer");
        job2.setStatus("Interview");

        when(jobApplicationRepository.findAll())
                .thenReturn(List.of(job1, job2));

        List<JobApplicationDTO> result =
                jobApplicationService.getAllJobsDTO();

        assertEquals(2, result.size());

        assertEquals("Google",
                result.get(0).getCompanyName());

        assertEquals("Software Engineer",
                result.get(0).getJobRole());

        assertEquals("Applied",
                result.get(0).getStatus());
    }

    @Test
    void shouldReturnDashboardStats() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn("ashish@gmail.com");

        User user = new User();
        user.setEmail("ashish@gmail.com");

        when(userRepository.findByEmail("ashish@gmail.com"))
                .thenReturn(user);

        when(jobApplicationRepository
                .countByUserAndStatus(user, "Applied"))
                .thenReturn(5L);

        when(jobApplicationRepository
                .countByUserAndStatus(user, "Interview"))
                .thenReturn(2L);

        when(jobApplicationRepository
                .countByUserAndStatus(user, "Offer"))
                .thenReturn(1L);

        when(jobApplicationRepository
                .countByUserAndStatus(user, "Rejected"))
                .thenReturn(3L);

        Map<String, Long> stats =
                jobApplicationService.getMyDashboardStats();

        assertEquals(5L, stats.get("Applied"));
        assertEquals(2L, stats.get("Interview"));
        assertEquals(1L, stats.get("Offer"));
        assertEquals(3L, stats.get("Rejected"));
    }
}