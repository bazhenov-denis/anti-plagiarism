package com.example.file_analysis_service;

import com.example.file_analysis_service.config.AnalysisStorageProperties;
import com.example.file_analysis_service.config.PathFileStoringProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({PathFileStoringProperties.class, AnalysisStorageProperties.class})
public class FileAnalysisServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileAnalysisServiceApplication.class, args);
	}

}
