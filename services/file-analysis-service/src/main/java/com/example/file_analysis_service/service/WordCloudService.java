package com.example.file_analysis_service.service;

import com.example.file_analysis_service.Api.FileStorageRestClient;
import com.example.file_analysis_service.Api.WordCloudApi;
import com.example.file_analysis_service.adapter.StorageAdapter;
import com.example.file_analysis_service.model.WordCloud;
import com.example.file_analysis_service.repository.WordCloudRepository;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WordCloudService {

  private final WordCloudRepository repo;
  private final FileStorageRestClient fileStorage;   // скачиваем .txt
  private final WordCloudApi quickChart;
  private final StorageAdapter storage;          // локальное хранилище

  @Transactional
  public ResponseEntity<ByteArrayResource> getWordCloud(Long fileId) {
    return repo.findByOriginalFileId(fileId)
        .map(wc -> pngResponse(loadSafe(wc.getPath())))
        .orElseGet(() -> buildAndSave(fileId));
  }

  /* --- helpers --- */

  @Transactional
  protected ResponseEntity<ByteArrayResource> buildAndSave(Long fileId) {
    // a) txt → text
    String text = new String(fileStorage.download(String.valueOf(fileId)), StandardCharsets.UTF_8);

    // b) QuickChart → png bytes
    byte[] png = quickChart.generatePng(text);

    // c) сохраняем локально
    String filename = fileId + "_cloud.png";
    try { storage.save(filename, png); }
    catch (IOException e) { throw new RuntimeException("Save PNG failed", e); }

    // d) в БД
    repo.save(WordCloud.builder()
        .originalFileId(fileId)
        .path(filename)
        .createdAt(Instant.now())
        .build());

    return pngResponse(png);
  }

  private byte[] loadSafe(String path) {
    try { return storage.load(path); }
    catch (IOException e) { throw new RuntimeException("Read PNG failed", e); }
  }

  private ResponseEntity<ByteArrayResource> pngResponse(byte[] img) {
    return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_PNG)
        .contentLength(img.length)
        .body(new ByteArrayResource(img));
  }
}