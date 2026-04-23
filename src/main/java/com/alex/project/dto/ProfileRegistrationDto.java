package com.alex.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ProfileRegistrationDto {

    @NotBlank
    @Email
    private String email;

    @Positive
    private Long id;

    public ProfileRegistrationDto(String email, Long id) {
        this.email = email;
        this.id = id;
    }

    public ProfileRegistrationDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
