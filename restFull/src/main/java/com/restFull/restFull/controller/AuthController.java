package com.restFull.restFull.controller;

import com.restFull.restFull.dtos.*;
import com.restFull.restFull.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO dto){
        RegisterResponseDTO register = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(register);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        LoginResponseDTO login = authService.login(dto);
        return ResponseEntity.ok(login);
    }
}