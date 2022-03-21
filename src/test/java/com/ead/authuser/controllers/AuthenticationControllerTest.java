package com.ead.authuser.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @LocalServerPort
    private Integer port;

    @Test
    public void testRegisterUser() {

         RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"username\": \"Ricardo\", \"email\": \"ricardo@uol.com.br\",\n" +
                        "            \"password\": \"545451\",\n" +
                        "            \"fullName\": \" Tonietti\",\n" +
                        "            \"cpf\": \"15697981848\" }")
                .when()
                    .post("http://localhost:" + port + "/auth/signup")
                .then()
                    .statusCode(201);
    }
}
