package com.example.demo.controllers;

import com.example.demo.models.Laptop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Se especificar webEnvironment para testear controlladores
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LaptopControllerTest {


    private TestRestTemplate testRestTemplate;

    //Inyeccion de dependencias
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:"+port);

        testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    @Test
    @DisplayName("Test hello method")
    void hello(){
        ResponseEntity<String> response = testRestTemplate.getForEntity("/saludo", String.class);
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals(400, response.getStatusCodeValue());
        assertEquals("Esto es un saludo desde Spring Boot", response.getBody() );
        assertTrue(response.hasBody());
    }

    @Test
    @DisplayName("Test Pedir todas las laptops")
    void findAll() {
        ResponseEntity<Laptop[]> response =
        testRestTemplate.getForEntity("/api/laptops", Laptop[].class);
        List<Laptop> laptops = Arrays.asList(response.getBody());
        System.out.println("Laptos size " +laptops.size());
        //assertTrue();
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals(400, response.getStatusCodeValue());
        assertTrue(response.hasBody());
    }

    @DisplayName("Test creacion de una Laptop")
    @Test
    void create() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String laptopJson= """
                {
                    "id": null,
                    "manufacture": "HP",
                    "model": "14BR2012",
                    "price": 2000000.0,
                    "inStock": true,
                    "memory": 8,
                    "storage": 250
                }
                
                """;

        HttpEntity<String> request = new HttpEntity<>(laptopJson,headers);

        ResponseEntity<Laptop> response =testRestTemplate.exchange("/api/laptops", HttpMethod.POST, request, Laptop.class);

        Laptop laptop = response.getBody();

        assertEquals(1L, laptop.getId());
    }

    @Test
    @DisplayName("Buscar por ID")
    void findOneById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String laptopJson = """
                                {
                                    "id": null,
                                    "manufacture": "HP",
                                    "model": "14BR2012",
                                    "price": 2000000.0,
                                    "inStock": true,
                                    "memory": 8,
                                    "storage": 250
                                }
                                """;
        HttpEntity<String> resquest = new HttpEntity<>(laptopJson, headers);
        ResponseEntity<Laptop> response = testRestTemplate.exchange("/api/laptops", HttpMethod.POST, resquest, Laptop.class);
        assertTrue(response.getBody().getId() == 1);



        ResponseEntity<Laptop> response1 = testRestTemplate.getForEntity("/api/laptops/1", Laptop.class);
        Laptop finalLaptop = response1.getBody();
        assertEquals( 8, finalLaptop.getMemory());



    }

    @Test
    @DisplayName("Test update laptop")
    void update() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String laptopJson = """
                                {
                                    "id": null,
                                    "manufacture": "HP",
                                    "model": "14BR2012",
                                    "price": 2000000.0,
                                    "inStock": true,
                                    "memory": 8,
                                    "storage": 250
                                }
                                """;
        HttpEntity<String> resquest = new HttpEntity<>(laptopJson, headers);
        ResponseEntity<Laptop> response = testRestTemplate.exchange("/api/laptops", HttpMethod.POST, resquest, Laptop.class);

        Laptop firstLaptop= response.getBody();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String laptopJson1 = """
                                {
                                    "id": 1,
                                    "manufacture": "HP1",
                                    "model": "14BR2012",
                                    "price": 2000000.0,
                                    "inStock": true,
                                    "memory": 12,
                                    "storage": 250
                                }
                                """;
        HttpEntity<String> resquest1 = new HttpEntity<>(laptopJson1, headers);
        ResponseEntity<Laptop> response1 = testRestTemplate.exchange("/api/laptops", HttpMethod.PUT, resquest1, Laptop.class);
        Laptop finalLaptop = response1.getBody();
        System.out.println(finalLaptop.getManufacture() +" "+ firstLaptop.getManufacture() );
        assertTrue(finalLaptop.getMemory() == 12);
        assertTrue(response1.getStatusCodeValue() == 200);
        assertTrue(response1.getStatusCodeValue() != 404);

    }

    @Test
    @DisplayName("Eliminacion de Laptop")
    void delete() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String laptopJson = """
                                {
                                    "id": null,
                                    "manufacture": "HP",
                                    "model": "14BR2012",
                                    "price": 2000000.0,
                                    "inStock": true,
                                    "memory": 16,
                                    "storage": 250
                                }
                                """;
        HttpEntity<String> resquest = new HttpEntity<>(laptopJson, headers);
        ResponseEntity<Laptop> response = testRestTemplate.exchange("/api/laptops", HttpMethod.POST, resquest, Laptop.class);
        response = testRestTemplate.exchange("/api/laptops", HttpMethod.POST, resquest, Laptop.class);
        Laptop firstLaptop= response.getBody();
        assertEquals(2, firstLaptop.getId());


        testRestTemplate.delete("/api/laptops/1");
        ResponseEntity<Laptop[]> finalresponse =
                testRestTemplate.getForEntity("/api/laptops", Laptop[].class);
        List<Laptop> laptops = Arrays.asList(finalresponse.getBody());
        System.out.println("Laptos size " +laptops.size());
        assertEquals(200, finalresponse.getStatusCodeValue());
        assertEquals(2, laptops.get(0).getId());

    }

    @Test
    @DisplayName("Eliminar todos los registros")
    void deleteAll() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String laptopJson = """
                                {
                                    "id": null,
                                    "manufacture": "HP",
                                    "model": "14BR2012",
                                    "price": 2000000.0,
                                    "inStock": true,
                                    "memory": 16,
                                    "storage": 250
                                }
                                """;
        HttpEntity<String> resquest = new HttpEntity<>(laptopJson, headers);
        ResponseEntity<Laptop> response = testRestTemplate.exchange("/api/laptops", HttpMethod.POST, resquest, Laptop.class);
        response = testRestTemplate.exchange("/api/laptops", HttpMethod.POST, resquest, Laptop.class);
        Laptop firstLaptop= response.getBody();
        assertEquals(2, firstLaptop.getId());


        testRestTemplate.delete("/api/laptops/");
        ResponseEntity<Laptop[]> finalresponse =
                testRestTemplate.getForEntity("/api/laptops", Laptop[].class);
        List<Laptop> laptops = Arrays.asList(finalresponse.getBody());
        System.out.println("Laptos size " +laptops.size());
        assertEquals(200, finalresponse.getStatusCodeValue());
        assertEquals(0, laptops.size());
    }
}