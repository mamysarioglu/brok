package com.brok.controller;

import com.brok.entity.Stock;
import com.brok.entity.User;
import com.brok.repository.StockRepository;
import com.brok.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        userRepository.deleteAll();
        var user = User.builder().username("test").roles("ROLE_USER").password(passwordEncoder.encode("test")).build();
        userRepository.save(user);
        var admin = User.builder().username("admintest").roles("ROLE_ADMIN").password(passwordEncoder.encode("admintest")).build();
        userRepository.save(admin);
    }


    @Test
    void getAllUsers() {
        RestAssured.given().contentType(ContentType.JSON)
                .auth().preemptive().basic("admintest", "admintest")
                .when()
                .get("/api/brokage/v1/users")
                .then()
                .statusCode(200);
    }


    @Test
    void getAllUsersAuthorizationDeniedExceptionExpected() {
        try {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().preemptive().basic("test", "test")
                    .when()
                    .get("/api/brokage/v1/users")
                    .then()
                    .statusCode(401);

        } catch (Exception e) {
            assertEquals(e.getMessage(), "status code: 500");
        }

    }
}