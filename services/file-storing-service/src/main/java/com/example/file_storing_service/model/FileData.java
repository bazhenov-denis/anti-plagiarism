package com.example.file_storing_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "file_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // или SEQUENCE
  private Long id;

  private String originalName;

  private String path;

  @Column(name = "content_type")
  private String contentType;

  @Enumerated(EnumType.STRING)
  private FileStatus status;

}