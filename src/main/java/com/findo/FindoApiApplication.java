package com.findo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
public class FindoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindoApiApplication.class, args);
    }

}
