package com.example.file_analysis_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "analysis.storage")
public class AnalysisStorageProperties {

  private String location;

}