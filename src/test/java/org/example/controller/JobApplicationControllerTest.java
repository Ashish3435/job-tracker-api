package org.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

class JobApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {

    }
    @Test
    void shouldReturnUnauthorizedWhenNoTokenProvided() throws Exception {

        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAccessProtectedEndpointWithJwtToken() throws Exception {

        String email =
                "test" + System.currentTimeMillis() + "@gmail.com";

        String registerJson = """
            {
              "name":"Test User",
              "email":"%s",
              "password":"password123"
            }
            """.formatted(email);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk());

        String loginJson = """
            {
              "email":"%s",
              "password":"password123"
            }
            """.formatted(email);

        String response =
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginJson))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        JsonNode jsonNode =
                objectMapper.readTree(response);

        String token = jsonNode.get("token").asText();

        mockMvc.perform(get("/api/jobs")
                        .header(
                                "Authorization",
                                "Bearer " + token))
                .andExpect(status().isOk());
    }
}