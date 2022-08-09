package com.example.demo;

import com.example.demo.models.Laptop;
import com.example.demo.repositories.LaptopRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DemoApplication.class, args);

		LaptopRepository laptopRepository = context.getBean(LaptopRepository.class);
		Laptop laptop1 = new Laptop(null, "Lenovo", "ThinkPad 3", 3500000.0, true, 12, 512);
		Laptop laptop2 = new Laptop(null, "ACER", "Nitro 3", 2300000.0, true, 16, 512);

		laptopRepository.save(laptop1);
		laptopRepository.save(laptop2);
	}

}
