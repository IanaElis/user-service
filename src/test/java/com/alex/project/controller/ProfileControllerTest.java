package com.alex.project.controller;

import com.alex.project.dto.ProfileDto;
import com.alex.project.dto.ProfileRegistrationDto;
import com.alex.project.dto.SearchUser;
import com.alex.project.entity.Alumni;
import com.alex.project.service.ProfileService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@QuarkusTest
public class ProfileControllerTest {

    @InjectMock
    ProfileService profileService;


    @Test
    void createProfile_shouldReturn200IfRegistrationSuccess(){
        String json = """
                    {
                        "email": "email@gmail.com",
                        "id": "2"
                    }               \s
               \s""";

        given()
                .contentType(io.restassured.http.ContentType.JSON)
                .body(json)
                .when()
                .post("/internal/profiles")
                .then()
                .statusCode(200);

        verify(profileService).createProfile(argThat(dto ->
                dto != null &&
                        "email@gmail.com".equals(dto.getEmail()) &&
                        dto.getId().equals(2L)
        ));
    }

    @Test
    void createProfile_shouldReturn400IfEmailIsBlank(){
        String json = """
                    {
                        "email": "",
                        "id": "2"
                    }               \s
               \s""";

        given()
                .contentType(io.restassured.http.ContentType.JSON)
                .body(json)
                .when()
                .post("/internal/profiles")
                .then()
                .statusCode(400);

        verify(profileService, never()).createProfile(any(ProfileRegistrationDto.class));
    }

    @Test
    void createProfile_shouldReturn400IfEmailInIncorrectFormat(){
        String json = """
                    {
                        "email": "email",
                        "id": "2"
                    }               \s
               \s""";

        given()
                .contentType(io.restassured.http.ContentType.JSON)
                .body(json)
                .when()
                .post("/internal/profiles")
                .then()
                .statusCode(400);

        verify(profileService, never()).createProfile(any(ProfileRegistrationDto.class));
    }

    @Test
    void createProfile_shouldReturn400IfIdInIncorrectFormat(){
        String json = """
                    {
                        "email": "email",
                        "id": "-5"
                    }               \s
               \s""";

        given()
                .contentType(io.restassured.http.ContentType.JSON)
                .body(json)
                .when()
                .post("/internal/profiles")
                .then()
                .statusCode(400);

        verify(profileService, never()).createProfile(any(ProfileRegistrationDto.class));
    }

    @Test
    void updateProfile_shouldReturn200AndPassHeaderToService() {
        String json = """
                {
                    "name": "Ivan",
                    "surname": "Ivanov",
                    "facultyNumber": 22220191,
                    "phoneNumber": "+359847346574",
                    "specialty": "SIT"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .header("X-USER-EMAIL", "test@gmail.com")
                .body(json)
                .when()
                .post("/internal/profiles/update")
                .then()
                .statusCode(200);

        verify(profileService).updateProfile(
                argThat(dto ->
                        dto != null &&
                                "Ivan".equals(dto.getName()) &&
                                "Ivanov".equals(dto.getSurname()) &&
                                dto.getFacultyNumber().equals(22220191L) &&
                                dto.getPhoneNumber().equals("+359847346574") &&
                                "SIT".equals(dto.getSpecialty())
                ),
                eq("test@gmail.com")
        );
    }

    @Test
    void updateProfile_shouldReturn400_whenBodyIsInvalid() {
        String json = """
                {
                    "name": "",
                    "surname": "Ivanov",
                    "facultyNumber": null,
                    "specialty": "SIT"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .header("X-USER-EMAIL", "test@gmail.com")
                .body(json)
                .when()
                .post("/internal/profiles/update")
                .then()
                .statusCode(400);

        verifyNoInteractions(profileService);
    }

    @Test
    void updateProfile_shouldPassNullToService_whenHeaderIsMissing() {
        String json = """
                {
                    "name": "Ivan",
                    "surname": "Ivanov",
                    "facultyNumber": 22220191,
                    "specialty": "SIT"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/internal/profiles/update")
                .then()
                .statusCode(400);

        verifyNoInteractions(profileService);
    }


    @Test
    void acceptChanges_shouldReturn200WhenRequestCorrect(){
        String json = """
                    {
                        "name": "Ivan",
                        "surname": "Ivanov",
                        "facultyNumber": 22220191,
                        "phoneNumber": "+359847346574",
                        "specialty": "SIT"
                    }
                """;

        given()
                .body(json)
                .contentType(ContentType.JSON)
                .when()
                .post("/internal/profiles/accept")
                .then()
                .statusCode(200);

        verify(profileService).acceptChanges(any(ProfileDto.class));
    }

    @Test
    void acceptChanges_shouldReturnBadRequestWhenValidationFailed(){
        String json = """
                    {
                        "name": "",
                        "surname": "",
                        "facultyNumber": 22220191,
                        "phoneNumber": "+359847346574",
                        "specialty": "SIT"
                    }
                """;

        given()
                .body(json)
                .contentType(ContentType.JSON)
                .when()
                .post("/internal/profiles/accept")
                .then()
                .statusCode(400);

        verifyNoInteractions(profileService);
    }

    @Test
    void rejectChanges_shouldReturn200(){
        String json = """
                    {
                        "email": "test@gmail.com"
                    }
                """;

        given()
                .body(json)
                .contentType(ContentType.JSON)
                .when()
                .post("/internal/profiles/reject")
                .then()
                .statusCode(200);

        verify(profileService).rejectChanges(any(SearchUser.class));
    }

    @Test
    void rejectChanges_shouldReturn400IfEmailIsBlank(){
        String json = """
                    {
                        "email": ""
                    }
                """;

        given()
                .body(json)
                .contentType(ContentType.JSON)
                .when()
                .post("/internal/profiles/reject")
                .then()
                .statusCode(400);

        verifyNoInteractions(profileService);
    }

    @Test
    void rejectChanges_shouldReturn400IfEmailIsInInvalidFormat(){
        String json = """
                    {
                        "email": "email"
                    }
                """;

        given()
                .body(json)
                .contentType(ContentType.JSON)
                .when()
                .post("/internal/profiles/reject")
                .then()
                .statusCode(400);

        verifyNoInteractions(profileService);
    }
}
