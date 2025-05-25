package com.example.file_analysis_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "file_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileStats {

  @Id
  private String fileId;
  private long chars;
  private long words;
  private long paragraphs;
}
