package com.example.file_storing_service.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.file_storing_service.FileStorageProperties;
import com.example.file_storing_service.exception.StorageException;
import com.example.file_storing_service.exception.StorageFileNotFoundException;

@Service
public class FileSystemStorageService implements FileStorageService {

  private final Path rootLocation;

  @Autowired
  public FileSystemStorageService(FileStorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation())
        .toAbsolutePath()
        .normalize();
  }

  @Override
  public void init() {
    try {
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }

  @Override
  public String store(MultipartFile file) {
    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file " + filename);
      }
      if (filename.contains("..")) {
        // Защита от path traversal
        throw new StorageException(
            "Cannot store file with relative path outside current directory " + filename);
      }

      Path destinationFile = rootLocation.resolve(filename).normalize();
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, destinationFile,
            StandardCopyOption.REPLACE_EXISTING);
      }
      return filename;
    } catch (IOException e) {
      throw new StorageException("Failed to store file " + filename, e);
    }
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(rootLocation, 1)
          .filter(path -> !path.equals(rootLocation))
          .map(rootLocation::relativize);
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }
  }

  @Override
  public Path load(String id) {
    return rootLocation.resolve(id).normalize();
  }

  @Override
  public Resource loadAsResource(String filename) {
    try {
      Path file = load(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() && resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException(
            "Could not read file: " + filename);
      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException(
          "Could not read file: " + filename, e);
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(rootLocation.toFile());
  }

  @Override
  public void deleteById(String id) {
    try {
      Path file = load(id);
      Files.deleteIfExists(file);
    } catch (IOException e) {
      throw new StorageException("Failed to delete file: " + id, e);
    }
  }
}
