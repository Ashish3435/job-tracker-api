package org.example.dto;

public class JobApplicationDTO {

    private String companyName;
    private String jobRole;
    private String status;

    public JobApplicationDTO() {
    }

    public JobApplicationDTO(String companyName,
                             String jobRole,
                             String status) {
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}