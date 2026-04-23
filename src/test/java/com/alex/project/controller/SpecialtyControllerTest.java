package com.alex.project.controller;

import com.alex.project.dto.FieldDto;
import com.alex.project.dto.SpecialtyDto;
import com.alex.project.entity.Field;
import com.alex.project.entity.Specialty;
import com.alex.project.service.api.SpecialityService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@QuarkusTest
public class SpecialtyControllerTest {

    @InjectMock
    SpecialityService service;

    @Test
    void getAll_shouldReturn200AndListOfSpecialty(){
        when(service.getAllSpecialty()).thenReturn(
                List.of(
                        new Specialty("SIT"),
                        new Specialty("Marketing")
                )
        );

        given()
                .when()
                .get("/internal/specialty/all")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].name", equalTo("SIT"))
                .body("[1].name", equalTo("Marketing"));
        verify(service).getAllSpecialty();
    }

    @Test
    void addNewSpecialty_shouldReturn200AndCallService() {
        String json = """
                {
                  "name": "SIT"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/internal/specialty/new")
                .then()
                .statusCode(200);

        verify(service).addNewSpeciality(any(SpecialtyDto.class));
    }

    @Test
    void addNewSpecialty_shouldPassCorrectDtoToService() {
        String json = """
                {
                  "name": "SIT"                
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/internal/specialty/new")
                .then()
                .statusCode(200);

        verify(service).addNewSpeciality(argThat(dto ->
                dto != null &&
                        "SIT".equals(dto.name())));
    }

    @Test
    void addNewSpecialty_shouldReturnBadRequestWhenNameIsBlank() {
        String json = """
                {
                  "name": ""                
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/internal/specialty/new")
                .then()
                .statusCode(400);

        verify(service, never()).addNewSpeciality(any(SpecialtyDto.class));
    }
}
