package com.example.file_analysis_service.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.file_analysis_service.model.WordCloud;

public interface WordCloudRepository extends JpaRepository<WordCloud, Long> {
  Optional<WordCloud> findByOriginalFileId(Long originalFileId);
}