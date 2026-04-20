package com.alex.project.utils;

import com.alex.project.entity.Field;
import com.alex.project.repository.FieldRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FiledMapperHelper {

    @Inject
    FieldRepository fieldRepository;

    public String map(Field field) {
        return field == null ? null : field.getName();
    }

    public Field map(String name) {
        if (name == null) return null;

        return fieldRepository.findByName(name).orElse(null);
    }
}