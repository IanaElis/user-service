package com.alex.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record FieldDto(@NotBlank String name, @Positive int number) {
}
