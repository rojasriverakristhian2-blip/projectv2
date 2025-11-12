package com.project.gestionconocimiento.project_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.project.gestionconocimiento.project_backend")
public class ProjectBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectBackendApplication.class, args);
    }
}