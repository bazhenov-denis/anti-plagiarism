package com.example.file_storing_service.service;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

// FileStorageService.java
public interface FileStorageService {
  /**
   * Сохраняет файл и возвращает имя сохранённого файла.
   */
  String store(MultipartFile file);

  void init();

  void deleteAll();

  void deleteById(String id);

  Path load(String id);

  Stream<Path> loadAll();

  Resource loadAsResource (String filename);
}