package com.example.file_storing_service.adapter;

import java.io.IOException;
import java.io.InputStream;

public interface StorageAdapter {

  void save(String fileName, InputStream data) throws IOException;

  void delete(String fileName);

  InputStream load(String fileName) throws IOException;
}
