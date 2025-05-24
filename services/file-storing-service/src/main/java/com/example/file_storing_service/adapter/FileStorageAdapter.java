package com.example.file_storing_service.adapter;

import com.example.file_storing_service.FileStorageProperties;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.FileSystemUtils;


@Component
@Slf4j
public class FileStorageAdapter implements StorageAdapter {

  private final Path rootLocation;

  public FileStorageAdapter(FileStorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
    init();
  }

  private void init() {
    try {
      if (Files.exists(rootLocation)) {
        // Рекурсивно удаляем все файлы и подпапки
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
        log.info("Очистка директории {}", rootLocation);
      }
      // Создаём пустую директорию
      Files.createDirectories(rootLocation);
      log.info("Создана директория для записи файлов по пути {}", rootLocation);
    } catch (IOException e) {
      log.error("Не удалось инициализировать хранилище по пути {}", rootLocation, e);
      throw new RuntimeException("Не удалось инициализировать хранилище по пути " + rootLocation, e);
    }
  }

  @Override
  public void save(String fileName, InputStream content) throws IOException {
    Assert.hasText(fileName, "Должно быть не пустое имя");
    Path path = rootLocation.resolve(fileName).normalize();
    if (!path.startsWith(rootLocation)) {
      throw new IOException("Некорректный ключ: " + fileName);
    }
    Files.copy(content, path, StandardCopyOption.REPLACE_EXISTING);
  }

  @Override
  public void delete(String key) {
    try {
      Path file = rootLocation.resolve(key).normalize();
      Files.deleteIfExists(file);
    } catch (IOException e) {
      // логируем и продолжаем, т.к. это «мягкая» операция
      System.err.println("Не удалось удалить файл " + key + ": " + e.getMessage());
    }
  }

  @Override
  public InputStream load(String key) throws IOException {
    Path file = rootLocation.resolve(key).normalize();
    if (!Files.exists(file) || !Files.isReadable(file)) {
      throw new NoSuchFileException("Файл не найден или недоступен: " + key);
    }
    return Files.newInputStream(file, StandardOpenOption.READ);
  }
}
