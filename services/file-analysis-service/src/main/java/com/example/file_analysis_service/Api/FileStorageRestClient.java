package com.example.file_analysis_service.Api;

import com.example.file_analysis_service.config.FileStoringProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FileStorageRestClient {

  private final RestTemplate restTemplate;
  private final FileStoringProperties props;

  /**
   * Скачивает содержимое файла по ID.
   * @param id UUID файла
   * @return байты файла
   */
  public byte[] download(String id) {
    // Собираем URL: http://<file.storing.url>/files/{id}/content
    String url = props.getUrl() + "/files/{id}";
    return restTemplate.getForObject(url, byte[].class, id);
  }
}