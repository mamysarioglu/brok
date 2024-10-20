package com.brok.controller;

import com.brok.entity.Stock;
import com.brok.entity.User;
import com.brok.repository.StockRepository;
import com.brok.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.internal.http.HttpResponseException;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AssetControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static Map<String, Object> reqBody = new HashMap<>();

    static {
        reqBody.put("size", 1000);
        reqBody.put("symbol", "THY");
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
        RestAssured.baseURI = "http://localhost:" + port;
        userRepository.deleteAll();
        stockRepository.deleteAll();

        var user = User.builder().username("test").roles("ROLE_USER").password(passwordEncoder.encode("test")).build();
        userRepository.save(user);


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
        stockRepository.save(apple);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void createAsset() {
        given().contentType(ContentType.JSON)
                .auth().preemptive().basic("test", "test")
                .body(reqBody)
                .when()
                .post("/api/brokage/v1/asset")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void getAllMyAsset() {
        createAsset();
        Response response = given().contentType(ContentType.JSON)
                .auth().preemptive().basic("test", "test")
                .when()
                .get("/api/brokage/v1/asset");

        response.prettyPrint();
        response.then().statusCode(HttpStatus.SC_OK);
        response.then().body("", hasSize(1));
        response.then().body("[0].size", equalTo(reqBody.get("size")));
        response.then().body("[0].symbol", equalTo(reqBody.get("symbol")));
    }

    @Test
    void getMyAsseBySymbol() {
        createAsset();
        Response response = given().contentType(ContentType.JSON)
                .auth().preemptive().basic("test", "test")
                .when()
                .get("/api/brokage/v1/asset/THY");
        response.prettyPrint();
        response.then().statusCode(HttpStatus.SC_OK);
        response.then().body("size", equalTo(reqBody.get("size")));
        response.then().body("symbol", equalTo(reqBody.get("symbol")));
        response.then().body("blockSize", equalTo(0));
    }

    @Test
    void getMyAsseBySymbolNotFoun() {
        createAsset();
        try {
            given().contentType(ContentType.JSON)
                    .auth().preemptive().basic("test", "test")
                    .when()
                    .get("/api/brokage/v1/asset/MIA");
        } catch (Exception e) {
            System.out.println("Caught HTTP Response Exception: " + e.getMessage());
            assertEquals(e.getMessage(), "status code: 500");
        }
    }
}