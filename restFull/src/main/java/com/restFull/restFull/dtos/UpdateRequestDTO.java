package com.restFull.restFull.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateRequestDTO(
        @NotBlank
        @Size(min = 6, max = 50)
        String username,

        @NotBlank
        @Size(min = 6, max = 100)
        String password
) {}