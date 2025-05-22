package com.example.file_storing_service.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.file_storing_service.exception.StorageFileNotFoundException;
import com.example.file_storing_service.service.FileStorageService;

@RestController
@RequestMapping("/files")
public class FileStorageController {

  private final FileStorageService storageService;

  @Autowired
  public FileStorageController(FileStorageService storageService) {
    this.storageService = storageService;
  }

  @PostConstruct
  public void init() {
    storageService.init();
  }

  /**
   * Список всех файлов (имён).
   */
  @GetMapping
  public ResponseEntity<List<String>> listFiles() throws IOException {
    List<String> files = storageService.loadAll()
        .map(path -> path.getFileName().toString())
        .collect(Collectors.toList());
    return ResponseEntity.ok(files);
  }

  /**
   * Загрузка файла.
   */
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> uploadFile(@RequestParam("file") MultipartFile file) {
    String filename = storageService.store(file);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{filename}")
        .buildAndExpand(filename)
        .toUri();
    return ResponseEntity.created(location).build();
  }

  /**
   * Отдача файла по имени.
   */
  @GetMapping("load/{filename:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
    Resource resource = storageService.loadAsResource(filename);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }

  /**
   * Удаление конкретного файла.
   */
  @DeleteMapping("delete/{filename:.+}")
  public ResponseEntity<Void> deleteFile(@PathVariable String filename) {
    storageService.deleteById(filename);
    return ResponseEntity.noContent().build();
  }

  /**
   * Удаление всех файлов.
   */
  @DeleteMapping
  public ResponseEntity<Void> deleteAllFiles() {
    storageService.deleteAll();
    return ResponseEntity.noContent().build();
  }

  /**
   * Обработка ошибки: файл не найден.
   */
  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity<String> handleNotFound(StorageFileNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
  }
}