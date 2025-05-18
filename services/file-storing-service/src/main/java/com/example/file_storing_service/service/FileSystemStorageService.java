package com.example.file_storing_service.service;

import com.example.file_storing_service.FileStorageProperties;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements FileStorageService {

  private final Path rootLocation;

  @Autowired
  public FileSystemStorageService(FileStorageProperties props) {
    this.rootLocation = Paths.get(props.getLocation()).toAbsolutePath().normalize();
    try {
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new RuntimeException("Невозможно создать директорию для хранения файлов", e);
    }
  }

  @Override
  public String store(MultipartFile file) {
    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    if (filename.contains("..")) {
      throw new RuntimeException("Некорректное имя файла: " + filename);
    }
    try (InputStream in = file.getInputStream()) {
      Path target = rootLocation.resolve(filename);
      Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
      return filename;
    } catch (IOException e) {
      throw new RuntimeException("Ошибка при сохранении файла " + filename, e);
    }
  }
}