package com.example.file_storing_service.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class AnalysisRestClientConfig {

  @Bean
  public RestClient analysisRestClient(RestClient.Builder builder) {
    return builder
        .baseUrl("http://file-analysis-service/analysis")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }
}