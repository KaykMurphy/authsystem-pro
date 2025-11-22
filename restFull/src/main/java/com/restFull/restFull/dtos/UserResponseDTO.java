package com.restFull.restFull.dtos;

import com.restFull.restFull.enums.Role;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponseDTO(
        Long id,
        String username,
        String email,
        Set<Role> role,
        LocalDateTime createdAt
) {}
