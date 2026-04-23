package com.alex.project.controller;

import com.alex.project.dto.FieldDto;
import com.alex.project.entity.Field;
import com.alex.project.service.api.FieldService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@QuarkusTest
public class FieldControllerTest {
    @InjectMock
    FieldService fieldService;

    @Test
    void getAllFields_shouldReturn200AndListOfFields() {
        when(fieldService.getAllFields()).thenReturn(
                List.of(
                        new Field("Backend", 1),
                        new Field("Frontend", 2)
                )
        );

        given()
                .when()
                .get("/internal/field/all")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].name", equalTo("Backend"))
                .body("[0].number", equalTo(1))
                .body("[1].name", equalTo("Frontend"))
                .body("[1].number", equalTo(2));

        verify(fieldService).getAllFields();
    }

    @Test
    void addNewField_shouldReturn200AndCallService() {
        String json = """
                {
                  "name": "Backend",
                  "number": 1
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/internal/field/new")
                .then()
                .statusCode(200);

        verify(fieldService).addNewField(any(FieldDto.class));
    }

    @Test
    void addNewField_shouldPassCorrectDtoToService() {
        String json = """
                {
                  "name": "Backend",
                  "number": 1
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/internal/field/new")
                .then()
                .statusCode(200);

        verify(fieldService).addNewField(argThat(dto ->
                dto != null &&
                        "Backend".equals(dto.name()) &&
                        dto.number() == 1
        ));
    }

    @Test
    void addNewField_ShouldReturnBadRequestWhenNumberIncorrect(){
        String json = """
                 {
                  "name": "Backend",
                  "number": -2
                }
                """;

        given()
                .body(json)
                .contentType(ContentType.JSON)
                .when()
                .post("/internal/field/new")
                .then()
                .statusCode(400);

        verify(fieldService, never()).addNewField(any(FieldDto.class));
    }

    @Test
    void addNewField_ShouldReturnBadRequestWhenNameIsBlank(){
        String json = """
                 {
                  "name": "",
                  "number": -2
                }
                """;

        given()
                .body(json)
                .contentType(ContentType.JSON)
                .when()
                .post("/internal/field/new")
                .then()
                .statusCode(400);

        verify(fieldService, never()).addNewField(any(FieldDto.class));
    }
}
