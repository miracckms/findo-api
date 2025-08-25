package com.findo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FindoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindoApiApplication.class, args);
    }

}
