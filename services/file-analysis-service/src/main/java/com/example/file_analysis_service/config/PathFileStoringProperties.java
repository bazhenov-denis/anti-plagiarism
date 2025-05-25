package com.example.file_analysis_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "file.storing")
public class PathFileStoringProperties {

  private String url;

  public String getLocation() {
    return null;
  }
}
