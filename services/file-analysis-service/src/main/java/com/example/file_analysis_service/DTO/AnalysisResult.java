package com.example.file_analysis_service.DTO;

import com.example.file_analysis_service.model.FileStats;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnalysisResult {
  private FileStats stats;
  private List<Similarity> similarity;
}