package com.alex.project.utils;

import com.alex.project.entity.Specialty;
import com.alex.project.repository.SpecialtyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SpecialtyMapperHelper {

    @Inject
    SpecialtyRepository specialtyRepository;

    public String map(Specialty specialty) {
        return specialty == null ? null : specialty.getName();
    }

    public Specialty map(String name) {
        if (name == null) return null;

        return specialtyRepository.findByName(name).orElse(null);
    }
}
