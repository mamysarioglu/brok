package com.brok.controller;

import com.brok.entity.Stock;
import com.brok.entity.User;
import com.brok.repository.StockRepository;
import com.brok.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StockControllerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private UUID STOCK_ID ;
    private static Map<String, Object> reqBody = new HashMap<>();

    static {
        reqBody.put("name", "Test");
        reqBody.put("symbol", "tst");
        reqBody.put("price", 100.0);
    }

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
        RestAssured.baseURI = "http://localhost:" + port + "/api/brokage";
        userRepository.deleteAll();
        stockRepository.deleteAll();

        var user = User.builder().username("test").roles("ROLE_USER").password(passwordEncoder.encode("test")).build();
        userRepository.save(user);

        var admin = User.builder().username("admintest").roles("ROLE_ADMIN").password(passwordEncoder.encode("admintest")).build();
        userRepository.save(admin);

        Stock TL = Stock.builder().name("Türk Lirası").symbol("TL").price(1).build();
        stockRepository.save(TL);
        Stock thy = Stock.builder().name("TURK HAVA YOLLAR").symbol("THY").price(100.5).build();
        stockRepository.save(thy);
        Stock mogan = Stock.builder().name("MOGAN").symbol("MOG").price(11).build();
        stockRepository.save(mogan);
        Stock tofas = Stock.builder().name("TOFAS").symbol("TOF").price(185.1).build();
        stockRepository.save(tofas);
        Stock mia = Stock.builder().name("MIA Teknoloji").symbol("MIA").price(40).build();
        stockRepository.save(mia);
        Stock apple = Stock.builder().name("APPLE").symbol("APP").price(10000).build();
        apple = stockRepository.save(apple);
        STOCK_ID = apple.getId();
    }
    @Test
    void getAllStocks() {
        Response response = given().contentType(ContentType.JSON)
                .auth().preemptive().basic("test", "test")
                .when()
                .get("/v1/stock");
        response.prettyPrint();
        response.then().statusCode(HttpStatus.SC_OK);

}
    @Test
    void createStock() {
        Response response = given().contentType(ContentType.JSON)
                .auth().preemptive().basic("admintest", "admintest")
                .body(reqBody)
                .when()
                .post("/v1/stock");
        response.prettyPrint();
        response.then().statusCode(200);
    }
    @Test
    void createStockError() {
        Response response = given().contentType(ContentType.JSON)
                .auth().preemptive().basic("test", "test")
                .body(reqBody)
                .when()
                .post("/v1/stock");
        response.prettyPrint();
        response.then().statusCode(500);
    }

    @Test
    void getStockById() {
        Response response = given().contentType(ContentType.JSON)
                .auth().preemptive().basic("test", "test")
                .when()
                .get("/v1/stock/"+STOCK_ID);
        response.prettyPrint();
        response.then().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(STOCK_ID.toString()))
                .body("symbol",equalTo("APP"));
    }
}