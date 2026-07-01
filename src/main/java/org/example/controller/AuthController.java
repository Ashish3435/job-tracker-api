package org.example.controller;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.example.security.JwtUtil;

import org.example.dto.MeResponse;
import org.springframework.security.core.Authentication;

import org.example.dto.LoginResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;



    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        User existingUser =
                userRepository.findByEmail(request.getEmail());

        if (existingUser != null) {
            return "Email already registered";
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole("USER");
        userRepository.save(user);

        return "User registered successfully";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail());
           System.out.println("LOGIN API HIT");

        if (user == null) {
            throw new RuntimeException("Invalid email");
        }

        boolean match = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!match) {
            throw new RuntimeException("Invalid password");
        }

        return new LoginResponse(
                jwtUtil.generateToken(
                        user.getEmail(),
                        user.getRole()
                )
        );
    }

    @GetMapping("/me")
    public MeResponse getCurrentUser(Authentication authentication) {

        String email = authentication.getName();

        return new MeResponse(email);
    }
}