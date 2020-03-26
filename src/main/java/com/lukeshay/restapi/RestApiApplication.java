package com.lukeshay.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.lukeshay.restapi"})
public class RestApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}
}
