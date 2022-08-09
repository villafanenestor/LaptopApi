package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {





    @Bean
    public OpenAPI openApiConfig(){
        return new OpenAPI().info(
                myCostumInfo
        );
    }





    private Info myCostumInfo = new Info()
            .title("Spring Boot API")
            .description("Documentacion de API-REST de Laptops ")
            .version("1.0")
            .contact(new Contact());


    private Contact myContact = new Contact()
            .email("mass.nestor@gmail.com")
            .name("Nestor Villafañe Mass Delevoper")
            .url("https:/google.com");
}
