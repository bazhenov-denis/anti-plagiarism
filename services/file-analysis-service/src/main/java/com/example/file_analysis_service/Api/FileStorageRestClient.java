package com.example.file_analysis_service.Api;

import com.example.file_analysis_service.config.PathFileStoringProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FileStorageRestClient {

  private final RestTemplate restTemplate;
  private final PathFileStoringProperties props;

  public FileStorageRestClient(RestTemplate restTemplate, PathFileStoringProperties props) {
    this.restTemplate = restTemplate;
    this.props = props;
  }

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