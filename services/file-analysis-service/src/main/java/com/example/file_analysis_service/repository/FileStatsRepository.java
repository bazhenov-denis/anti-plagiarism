package com.example.file_analysis_service.repository;


import com.example.file_analysis_service.model.FileStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileStatsRepository extends JpaRepository<FileStats, String> {
}