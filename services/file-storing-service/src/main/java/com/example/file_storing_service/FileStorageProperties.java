package com.example.file_storing_service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageProperties {

  private String location;

  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }
}