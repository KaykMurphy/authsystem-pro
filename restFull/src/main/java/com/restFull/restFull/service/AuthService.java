package com.restFull.restFull.service;

import com.restFull.restFull.dtos.*;
import com.restFull.restFull.entity.User;
import com.restFull.restFull.enums.Role;
import com.restFull.restFull.exceptions.EmailAlreadyExistsException; // REFATORAÇÃO: Correção de typo no pacote
import com.restFull.restFull.exceptions.UsernameAlreadyExistsException;
import com.restFull.restFull.security.JwtUtils;
import com.restFull.restFull.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // REFATORAÇÃO: Import Transactional

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO dto){

        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email já está em uso.");
        }

        if (userRepository.existsByUsername(dto.username())) {
            throw new UsernameAlreadyExistsException("Username já está em uso.");
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Collections.singleton(Role.USER));

        User saved = userRepository.save(user);

        String roles = extractRoles(saved);
        String token = jwtUtils.generateToken(dto.email(), roles);

        return new RegisterResponseDTO(
                saved.getId(),
                saved.getEmail(),
                saved.getUsername(),
                saved.getCreatedAt(),
                saved.getRole(),
                token
        );
    }

    public LoginResponseDTO login(LoginRequestDTO dto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String roles = extractRoles(user);
        String token = jwtUtils.generateToken(dto.email(), roles);

        return new LoginResponseDTO(
                user.getId(),
                token,
                user.getEmail(),
                user.getUsername(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    private String extractRoles(User user) {
        return user.getRole().stream()
                .map(Role::getValue)
                .collect(Collectors.joining(","));
    }
}