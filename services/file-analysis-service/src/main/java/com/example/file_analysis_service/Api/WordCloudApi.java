package com.example.file_analysis_service.Api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WordCloudApi {

  private final RestTemplate rest;
  private final ObjectMapper mapper = new ObjectMapper();
  private final String url = "https://quickchart.io/wordcloud";

  public byte[] generatePng(String text) {
    // POST json — лучше, чем длинный GET-query
    Map<String, Object> body = Map.of(
        "format", "png",
        "width", 600,
        "height", 600,
        "text", text
    );

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> req = new HttpEntity<>(json(body), headers);
    ResponseEntity<byte[]> resp =
        rest.postForEntity(url, req, byte[].class);

    return resp.getBody();
  }

  private String json(Object o) {
    try { return mapper.writeValueAsString(o); }
    catch (Exception e) { throw new RuntimeException(e); }
  }
}
