package com.restFull.restFull.dtos;

import com.restFull.restFull.enums.Role;

import java.time.LocalDateTime;
import java.util.Set;

public record RegisterResponseDTO(

        Long id,
        String email,
        String username,
        LocalDateTime createdIt,
        Set<Role> role,
        String token

) {
}
