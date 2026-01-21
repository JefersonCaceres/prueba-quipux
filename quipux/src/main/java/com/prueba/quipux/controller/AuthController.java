package com.prueba.quipux.controller;

import com.prueba.quipux.dto.LoginRequest;
import com.prueba.quipux.dto.LoginResponse;
import com.prueba.quipux.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (!"admin".equals(request.username()) || !"admin123".equals(request.password())) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtService.generateToken(request.username());
        return ResponseEntity.ok(new LoginResponse(token, "Bearer"));
    }
}
