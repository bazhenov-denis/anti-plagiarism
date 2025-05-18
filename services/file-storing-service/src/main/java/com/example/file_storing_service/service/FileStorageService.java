package com.example.file_storing_service.service;

import org.springframework.web.multipart.MultipartFile;

// FileStorageService.java
public interface FileStorageService {
  /**
   * Сохраняет файл и возвращает имя сохранённого файла.
   */
  String store(MultipartFile file);
}