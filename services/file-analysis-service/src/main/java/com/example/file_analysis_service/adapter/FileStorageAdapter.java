package com.example.file_analysis_service.adapter;

import com.example.file_analysis_service.config.AnalysisStorageProperties;
import com.example.file_analysis_service.config.PathFileStoringProperties;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
@Slf4j
public class FileStorageAdapter implements StorageAdapter {

  private final Path root;

  public FileStorageAdapter(AnalysisStorageProperties props) {
    String loc = Objects.requireNonNull(
        props.getLocation(),
        "Property 'analysis.storage.location' must be set");
    this.root = Paths.get(loc);
    init();
  }

  private void init() {
    try {
      Files.createDirectories(root);
      log.info("Word-cloud storage dir: {}", root);
    } catch (IOException e) {
      throw new IllegalStateException("Cannot init storage dir", e);
    }
  }

  @Override
  public void save(String key, byte[] data) throws IOException {
    Files.write(root.resolve(key), data, StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);
  }

  @Override
  public byte[] load(String key) throws IOException {
    return Files.readAllBytes(root.resolve(key));
  }
}
