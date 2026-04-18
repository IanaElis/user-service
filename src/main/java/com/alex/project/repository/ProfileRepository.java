package com.alex.project.repository;

import com.alex.project.entity.Alumni;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ProfileRepository implements PanacheRepository<Alumni> {
    public Optional<Alumni> findByName(String name, String surname){return find("name = :name and surname = :surname",
            Parameters.with("name", name)
                    .and("surname", surname))
            .firstResultOptional();};

    public Optional<Alumni> findByEmail(String email){
        return find("email = :email",
                Parameters.with("email", email))
                .firstResultOptional();
    }


}
