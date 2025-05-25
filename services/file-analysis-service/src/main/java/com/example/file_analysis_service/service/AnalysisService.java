package com.example.file_analysis_service.service;

import com.example.file_analysis_service.Api.FileStorageRestClient;
import com.example.file_analysis_service.DTO.AnalysisResult;
import com.example.file_analysis_service.DTO.Similarity;
import com.example.file_analysis_service.model.FileStats;
import com.example.file_analysis_service.repository.FileStatsRepository;
import com.example.file_analysis_service.lucene.LuceneIndexer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalysisService {
  private final FileStorageRestClient storageClient;
  private final FileStatsRepository statsRepo;
  private final LuceneIndexer lucene;

  @Transactional
  public AnalysisResult analyzeAndGetAll(String fileId) {
    FileStats stats = statsRepo.findById(fileId)
        .orElseGet(() -> {
          byte[] data = storageClient.download(fileId);
          String text = new String(data, StandardCharsets.UTF_8);
          FileStats s = new FileStats(fileId,
              text.length(),
              countWords(text),
              countParagraphs(text)
          );
          return statsRepo.save(s);
        });

    byte[] data = storageClient.download(fileId);
    String text = new String(data, StandardCharsets.UTF_8);
    lucene.indexDocument(fileId, text);

    List<Similarity> sim = lucene.findSimilar(fileId, 5);

    return new AnalysisResult(stats, sim);
  }

  private long countWords(String text) {
    return text.split("\\W+").length;
  }

  private long countParagraphs(String text) {
    return text.split("(\\r?\\n){2,}").length;
  }
}
