package com.alex.project.utils;

import com.alex.project.entity.Field;
import com.alex.project.entity.Specialty;
import com.alex.project.repository.FiledRepository;
import com.alex.project.repository.SpecialtyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FiledMapperHelper {

    @Inject
    FiledRepository filedRepository;

    public String map(Field field) {
        return field == null ? null : field.getName();
    }

    public Field map(String name) {
        if (name == null) return null;

        return filedRepository.findByName(name).orElse(null);
    }
}
