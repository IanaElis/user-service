package com.alex.project.service.api;

import com.alex.project.dto.SpecialtyDto;
import com.alex.project.entity.Specialty;
import com.alex.project.exceptions.service.RecordAlreadyExistException;
import com.alex.project.repository.SpecialtyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.wildfly.common.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class SpecialtyServiceTest {

    @Mock
    SpecialtyRepository repository;

    @InjectMocks
    SpecialityService specialtyService;

    private SpecialtyDto specialtyDto;

    @BeforeEach
    void setUp() {
        specialtyDto = new SpecialtyDto("Test Specialty");
    }

    @Test
    void addNewField_whenFieldWithSameNameAlreadyExists() {
        Specialty existingSpecialty = new Specialty("Test Field");
        when(repository.findByName(specialtyDto.name())).thenReturn(Optional.of(existingSpecialty));

        RecordAlreadyExistException exception = assertThrows(
                RecordAlreadyExistException.class,
                () -> specialtyService.addNewSpeciality(specialtyDto)
        );

        assertEquals("This specialty already exist", exception.getMessage());

        verify(repository).findByName(specialtyDto.name());
        verify(repository, never()).persist(any(Specialty.class));
    }

    @Test
    void addNewField_whenFieldWithSameNameDoesNotExist() {
        when(repository.findByName(specialtyDto.name())).thenReturn(Optional.empty());

        ArgumentCaptor<Specialty> specialtyCaptor = ArgumentCaptor.forClass(Specialty.class);

        specialtyService.addNewSpeciality(specialtyDto);

        verify(repository).findByName(specialtyDto.name());
        verify(repository).persist(specialtyCaptor.capture());

        Specialty savedField = specialtyCaptor.getValue();
        assertNotNull(savedField);
        assertEquals(specialtyDto.name(), savedField.getName());
    }

    @Test
    void getAllFields_shouldReturnAllFieldsFromRepository() {
        List<Specialty> expectedFields = List.of(
                new Specialty("Field 1"),
                new Specialty("Field 2")
        );

        when(repository.listAll()).thenReturn(expectedFields);

        List<Specialty> actualFields = specialtyService.getAllSpecialty();

        assertEquals(expectedFields, actualFields);
        verify(repository).listAll();
    }
}
