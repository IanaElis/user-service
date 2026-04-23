package com.alex.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SearchUser(@NotBlank @Email String email) {
}
