package com.alex.project.repository;

import com.alex.project.entity.Alumni;
import com.alex.project.entity.Specialty;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SpecialtyRepository implements PanacheRepository<Specialty> {

    public Optional<Specialty> findByName(String name) {
        return find("name = :name",
                Parameters.with("name", name))
                .firstResultOptional();
    }
}
