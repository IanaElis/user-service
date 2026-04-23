package com.alex.project.service.api;

import com.alex.project.dto.FieldDto;
import com.alex.project.entity.Field;
import com.alex.project.exceptions.service.RecordAlreadyExistException;
import com.alex.project.repository.FieldRepository;
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
import static org.mockito.Mockito.*;
import static org.wildfly.common.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class FieldServiceTest {

    @Mock
    FieldRepository repository;

    @InjectMocks
    FieldService fieldService;

    private FieldDto fieldDto;

    @BeforeEach
    void setUp() {
        fieldDto = new FieldDto("Test Field", 7);
    }

    @Test
    void addNewField_whenFieldWithSameNameAlreadyExists() {
        Field existingField = new Field("Test Field", 99);
        when(repository.findByName(fieldDto.name())).thenReturn(Optional.of(existingField));

        RecordAlreadyExistException exception = assertThrows(
                RecordAlreadyExistException.class,
                () -> fieldService.addNewField(fieldDto)
        );

        assertEquals("This field already exist", exception.getMessage());

        verify(repository).findByName(fieldDto.name());
        verify(repository, never()).persist(any(Field.class));
    }

    @Test
    void addNewField_whenFieldWithSameNameDoesNotExist() {
        when(repository.findByName(fieldDto.name())).thenReturn(Optional.empty());

        ArgumentCaptor<Field> fieldCaptor = ArgumentCaptor.forClass(Field.class);

        fieldService.addNewField(fieldDto);

        verify(repository).findByName(fieldDto.name());
        verify(repository).persist(fieldCaptor.capture());

        Field savedField = fieldCaptor.getValue();
        assertNotNull(savedField);
        assertEquals(fieldDto.name(), savedField.getName());
        assertEquals(fieldDto.number(), savedField.getNumber());
    }

    @Test
    void getAllFields_shouldReturnAllFieldsFromRepository() {
        List<Field> expectedFields = List.of(
                new Field("Field 1", 1),
                new Field("Field 2", 2)
        );

        when(repository.listAll()).thenReturn(expectedFields);

        List<Field> actualFields = fieldService.getAllFields();

        assertEquals(expectedFields, actualFields);
        verify(repository).listAll();
    }
}
