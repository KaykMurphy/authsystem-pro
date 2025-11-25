package com.restFull.restFull.dtos;

public record UpdateResponseDTO(
        Long id,
        String username,
        String email
) {}