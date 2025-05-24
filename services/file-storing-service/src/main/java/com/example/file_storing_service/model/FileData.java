package com.example.file_storing_service.model;

import jakarta.persistence.Entity;
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
  @GeneratedValue(strategy = GenerationType.AUTO)
  String id;

  String originalName;

  String path;

  Enum<FileStatus> status;

}