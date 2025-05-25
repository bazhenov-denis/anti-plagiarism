package com.example.file_analysis_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file.storing")
public class FileStoringProperties {
  /**
   * Ожидается, что в application.yml/properties будет что-то вроде:
   * file.storing.url=http://localhost:8080
   */
  private String url;
  public String getUrl() { return url; }
  public void setUrl(String url) { this.url = url; }
}
