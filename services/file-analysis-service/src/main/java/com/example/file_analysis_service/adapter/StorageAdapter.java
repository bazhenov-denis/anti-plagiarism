package com.example.file_analysis_service.adapter;

import java.io.IOException;
import java.io.InputStream;

public interface StorageAdapter {
  void save(String key, byte[] data) throws IOException;
  byte[] load(String key) throws IOException;
}