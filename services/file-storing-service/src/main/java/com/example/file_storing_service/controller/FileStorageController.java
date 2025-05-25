package com.example.file_storing_service.controller;

import com.example.file_storing_service.controller.DTO.FileInfoDto;
import com.example.file_storing_service.exception.StorageFileNotFoundException;
import com.example.file_storing_service.service.FileStorageService;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileStorageController {

  private final FileStorageService service;

  public FileStorageController(FileStorageService service) {
    this.service = service;
  }

  /** 1) Загрузка */
  @PostMapping
  public FileInfoDto upload(@RequestParam("file") MultipartFile file) {
    return service.store(file);                 // store() теперь отдаёт DTO с Long id
  }

  /** 2) Список всех файлов (id + originalName) */
  @GetMapping
  public List<FileInfoDto> listFiles() {
    return service.listAll();
  }

  /** 3) Скачивание */
  @GetMapping("/{id}")
  public ResponseEntity<Resource> download(@PathVariable Long id) {
    Resource resource = service.downloadAsResource(id);

    String filename = service.listAll().stream()
        .filter(dto -> dto.getId().equals(id))
        .map(FileInfoDto::getOriginalName)
        .findFirst()
        .orElse("file-" + id);                  // fallback-имя, если не нашли

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + filename + "\"")
        .body(resource);
  }

  /** 4) Удаление */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  /** 5) Обработка «файл не найден» */
  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity<Void> handleNotFound() {
    return ResponseEntity.notFound().build();
  }
}
