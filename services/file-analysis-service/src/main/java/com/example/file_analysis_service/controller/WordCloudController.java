package com.example.file_analysis_service.controller;

import com.example.file_analysis_service.service.WordCloudService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class WordCloudController {

  private final WordCloudService service;

  /** GET /analysis/{id}/wordcloud -> PNG */
  @GetMapping("/{id}/wordcloud")
  public ResponseEntity<ByteArrayResource> wordCloud(@PathVariable Long id) {
    return service.getWordCloud(id);
  }
}