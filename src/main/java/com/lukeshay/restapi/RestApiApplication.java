package com.lukeshay.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan({"com.lukeshay.restapi"})
@EnableAsync
@EnableScheduling
public class RestApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestApiApplication.class, args);
  }
}
