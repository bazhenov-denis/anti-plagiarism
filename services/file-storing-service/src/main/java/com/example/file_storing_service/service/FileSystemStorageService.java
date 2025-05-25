package com.example.file_storing_service.service;

import com.example.file_storing_service.Repository.FileDataRepository;
import com.example.file_storing_service.adapter.StorageAdapter;
import com.example.file_storing_service.controller.DTO.FileInfoDto;
import com.example.file_storing_service.model.FileData;
import com.example.file_storing_service.model.FileStatus;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.file_storing_service.FileStorageProperties;
import com.example.file_storing_service.exception.StorageException;
import com.example.file_storing_service.exception.StorageFileNotFoundException;

@Service
@Slf4j
public class FileSystemStorageService implements FileStorageService {

  private final Path rootLocation;
  private final FileDataRepository fileDataRepository;
  private final StorageAdapter storageAdapter;

  @Autowired
  public FileSystemStorageService(FileStorageProperties properties, FileDataRepository fileDataRepository, StorageAdapter storageAdapter) {
    this.fileDataRepository = fileDataRepository;
    this.rootLocation = Paths.get(properties.getLocation())
        .toAbsolutePath()
        .normalize();
    this.storageAdapter = storageAdapter;
  }


  @Override
  @Transactional
  public FileInfoDto store(MultipartFile file) {
    String fileId = UUID.randomUUID().toString();

    try (InputStream in = file.getInputStream()) {
      storageAdapter.save(fileId, in);
    } catch (IOException e) {
      log.error("Ошибка при сохранении файла в адаптер: {}", e.getMessage(), e);
      throw new RuntimeException("Не удалось сохранить файл", e);
    }

    FileData fileData = FileData.builder()
        .id(fileId)
        .originalName(file.getOriginalFilename())
        .path(fileId)
        .status(FileStatus.NEW)
        .build();
    fileDataRepository.save(fileData);
    return new FileInfoDto(fileId, fileData.getOriginalName());
  }

  @Override
  @Transactional(readOnly = true)
  public List<FileInfoDto> listAll() {
    return fileDataRepository.findAll().stream()
        .map(fd -> new FileInfoDto(fd.getId(), fd.getOriginalName()))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Resource downloadAsResource(String id) {
    var meta = fileDataRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Файл не найден: " + id));
    try {
      var in = storageAdapter.load(meta.getPath());
      return new InputStreamResource(in);
    } catch (IOException e) {
      log.error("Не удалось прочитать файл {}: {}", id, e.getMessage(), e);
      throw new RuntimeException("Ошибка чтения файла", e);
    }
  }

  @Transactional
  public void delete(String id) {
    var meta = fileDataRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Файл не найден: " + id));
    storageAdapter.delete(meta.getPath());
    fileDataRepository.delete(meta);
  }



}
