package com.example.file_analysis_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Similarity {
  private String fileId;
  private double score;
}

