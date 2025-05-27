package com.example.file_storing_service.service;

import com.example.file_storing_service.controller.DTO.FileInfoDto;
import com.example.file_storing_service.model.FileData;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

// FileStorageService.java
public interface FileStorageService {
  /**
   * Сохраняет файл и возвращает имя сохранённого файла.
   */
  FileInfoDto store(MultipartFile file) throws IOException;

  String getContentType(Long id);

  FileData getMeta(Long id);

  List<FileInfoDto> listAll();

  Resource downloadAsResource(Long id);

  void delete(Long id);

}