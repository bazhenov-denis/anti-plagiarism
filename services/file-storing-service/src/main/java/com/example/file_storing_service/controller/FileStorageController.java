package com.example.file_storing_service.controller;

import com.example.file_storing_service.controller.DTO.FileUploadResponse;
import com.example.file_storing_service.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/files")
public class FileStorageController {

  private final FileStorageService storageService;

  @Autowired
  public FileStorageController(FileStorageService storageService) {
    this.storageService = storageService;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<FileUploadResponse> upload(@RequestParam("file") MultipartFile file) {
    String savedFilename = storageService.store(file);
    // Собираем URL до файла, если нужно
    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/files/")
        .path(savedFilename)
        .toUriString();
    FileUploadResponse resp = new FileUploadResponse(
        savedFilename, fileDownloadUri, file.getContentType(), file.getSize()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(resp);
  }
}