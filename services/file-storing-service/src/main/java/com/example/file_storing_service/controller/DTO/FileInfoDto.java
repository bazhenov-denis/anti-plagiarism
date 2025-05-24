package com.example.file_storing_service.controller.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileInfoDto {
  private String id;
  private String originalName;
}