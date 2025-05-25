// src/main/java/com/example/file_analysis_service/model/WordCloud.java
package com.example.file_analysis_service.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity @Table(name = "word_cloud")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WordCloud {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private Long originalFileId;

  @Column(nullable = false, length = 200)
  private String path;

  private Instant createdAt;
}
