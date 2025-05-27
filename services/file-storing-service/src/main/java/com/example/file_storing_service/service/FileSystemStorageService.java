package com.example.file_storing_service.service;

import com.example.file_storing_service.Repository.FileDataRepository;
import com.example.file_storing_service.adapter.StorageAdapter;
import com.example.file_storing_service.controller.DTO.FileInfoDto;
import com.example.file_storing_service.exception.StorageFileNotFoundException;
import com.example.file_storing_service.model.FileData;
import com.example.file_storing_service.model.FileStatus;
import org.apache.tika.Tika;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
public class FileSystemStorageService implements FileStorageService {

  private final FileDataRepository fileDataRepository;
  private final StorageAdapter storageAdapter;
  private final RestClient analysisRestClient;   // ← внедряем RestClient


  @Autowired
  public FileSystemStorageService(FileDataRepository fileDataRepository, StorageAdapter storageAdapter,
      RestClient analysisRestClient
  ) {
    this.fileDataRepository = fileDataRepository;
    this.storageAdapter = storageAdapter;
    this.analysisRestClient = analysisRestClient;
  }

  @Transactional(readOnly = true)
  public String getContentType(Long id) {
    return fileDataRepository.findById(id)
        .map(FileData::getContentType)
        .orElse("application/octet-stream");
  }

  @Override
  @Transactional(readOnly = true)
  public FileData getMeta(Long id) {
    return fileDataRepository.findById(id)
        .orElseThrow(() ->
            new StorageFileNotFoundException("Файл " + id + " не найден"));
  }

  @Override
  @Transactional
  public FileInfoDto store(MultipartFile file) throws IOException {

    // (а) определяем MIME + charset
    String detected = new Tika().detect(file.getInputStream());          // «text/plain; charset=UTF-16LE»

    // (б) пишем метаданные
    FileData meta = FileData.builder()
        .originalName(file.getOriginalFilename())
        .contentType(detected)
        .status(FileStatus.NEW)
        .build();
    fileDataRepository.saveAndFlush(meta);

    String storageKey = meta.getId().toString();

    // (в) сам файл
    try (InputStream in = file.getInputStream()) {
      storageAdapter.save(storageKey, in);
    }

    meta.setPath(storageKey);

    triggerAnalysis(meta.getId());
    return new FileInfoDto(meta.getId().toString(), meta.getOriginalName());
  }

  @Async("analysisExecutor")
  @Transactional          // каждая асинхронная задача — своя транзакция
  public void triggerAnalysis(Long id) {

    try {
      analysisRestClient.post()
          .uri("/{id}", id)
          .retrieve()
          .toBodilessEntity();               // ждём 2xx

      // ← если дошли до сюда, вызов успешен
      fileDataRepository.findById(id).ifPresent(file -> file.setStatus(FileStatus.INDEXED));
      log.info("Файл {} успешно проиндексирован и помечен как INDEXED", id);

    } catch (Exception ex) {
      // 4xx/5xx, сетевые или другие ошибки
      fileDataRepository.findById(id).ifPresent(file -> file.setStatus(FileStatus.FAILED));
      log.error("Анализ файла {} закончился ошибкой: {}", id, ex.getMessage(), ex);
    }
    // @Transactional гарантирует commit или rollback в конце метода
  }

  @Override
  @Transactional(readOnly = true)
  public List<FileInfoDto> listAll() {
    return fileDataRepository.findAll().stream()
        .map(fd -> new FileInfoDto(fd.getId().toString(), fd.getOriginalName()))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Resource downloadAsResource(Long id) {
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
  public void delete(Long id) {
    var meta = fileDataRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Файл не найден: " + id));
    storageAdapter.delete(meta.getPath());
    fileDataRepository.delete(meta);
  }



}
