package org.example.dto;

public class MeResponse {

    private String email;

    public MeResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}