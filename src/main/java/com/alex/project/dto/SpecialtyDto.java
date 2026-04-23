package com.alex.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SpecialtyDto(@NotBlank String name) {
}
