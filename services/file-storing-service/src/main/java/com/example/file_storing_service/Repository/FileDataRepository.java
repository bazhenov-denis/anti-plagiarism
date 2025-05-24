package com.example.file_storing_service.Repository;

import com.example.file_storing_service.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDataRepository extends JpaRepository<FileData, String> {
}
