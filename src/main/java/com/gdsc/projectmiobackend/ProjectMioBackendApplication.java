package com.gdsc.projectmiobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProjectMioBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectMioBackendApplication.class, args);
    }

}
