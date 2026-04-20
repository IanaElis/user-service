package com.alex.project.service.api;

import com.alex.project.dto.SpecialtyDto;
import com.alex.project.entity.Specialty;
import com.alex.project.exceptions.service.RecordAlreadyExistException;
import com.alex.project.repository.SpecialtyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class SpecialityService {
    @Inject
    SpecialtyRepository repository;

    @Transactional
    public void addNewSpeciality(SpecialtyDto specialtyDto){
        if(repository.findByName(specialtyDto.name()).isPresent()){
            throw new RecordAlreadyExistException("This speciality already exist");
        }

        repository.persist(new Specialty(specialtyDto.name()));
    }

    public List<Specialty> getAllSpecialty(){
        return repository.listAll();
    }
}
