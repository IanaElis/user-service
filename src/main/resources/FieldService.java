package com.alex.project.service.api;

import com.alex.project.dto.FieldDto;
import com.alex.project.entity.Field;
import com.alex.project.exceptions.service.RecordAlreadyExistException;
import com.alex.project.repository.FieldRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class FieldService {

    @Inject
    FieldRepository repository;

    @Transactional
    public void addNewField(FieldDto fieldDto){
        if(repository.findByName(fieldDto.name()).isPresent()){
            throw new RecordAlreadyExistException("This field already exist!");
        }

        repository.persist(new Field(fieldDto.name(), fieldDto.number()));
    }

    public List<Field> getAllFields(){
        return repository.listAll();
    }
}
