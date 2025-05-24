package com.example.file_storing_service;

import com.example.file_storing_service.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FileStoringServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileStoringServiceApplication.class, args);
	}


}
