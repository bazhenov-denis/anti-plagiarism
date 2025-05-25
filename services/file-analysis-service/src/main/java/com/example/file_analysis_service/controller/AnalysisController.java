package com.example.file_analysis_service.controller;

import com.example.file_analysis_service.DTO.AnalysisResult;
import com.example.file_analysis_service.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {
  private final AnalysisService analysisService;

  @PostMapping("/{id}")
  public AnalysisResult analyze(@PathVariable String id) {
    return analysisService.analyzeAndGetAll(id);
  }
}
