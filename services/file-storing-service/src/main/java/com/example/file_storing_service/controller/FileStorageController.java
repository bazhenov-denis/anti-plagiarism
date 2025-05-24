package com.example.file_storing_service.controller;

import com.example.file_storing_service.controller.DTO.FileInfoDto;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
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

  private final FileStorageService service;

  @Autowired
  public FileStorageController(FileStorageService service) {
    this.service = service;
  }

  // 1) Загрузка
  @PostMapping
  public FileInfoDto upload(@RequestParam("file") MultipartFile file) {
    return service.store(file);
  }

  // 2) Список (id + оригинальное имя)
  @GetMapping
  public List<FileInfoDto> listFiles() {
    return service.listAll();
  }

  // 3) Скачивание
  @GetMapping("/{id}")
  public ResponseEntity<Resource> download(@PathVariable String id) {
    Resource resource = service.downloadAsResource(id);
    String filename = service.listAll().stream()
        .filter(dto -> dto.getId().equals(id))
        .map(FileInfoDto::getOriginalName)
        .findFirst()
        .orElse(id);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
        .body(resource);
  }

  // 4) Удаление
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}