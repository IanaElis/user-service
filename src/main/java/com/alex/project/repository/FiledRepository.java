package com.alex.project.repository;

import com.alex.project.entity.Field;
import com.alex.project.entity.Specialty;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class FiledRepository implements PanacheRepository<Field> {

    public Optional<Field> findByName(String name) {
        return find("name = :name",
                Parameters.with("name", name))
                .firstResultOptional();
    }
}
