package com.restFull.restFull.dtos;

import com.restFull.restFull.enums.Role;

import java.time.LocalDateTime;
import java.util.Set;

public record LoginResponseDTO(
        Long id,
        String token,
        String email,
        String username,
        Set<Role> roles,
        LocalDateTime createdIt
) {
}
