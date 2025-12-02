package com.vibetrack.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class VibetrackBackendApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(VibetrackBackendApplication.class, args);
	}

}
