package com.projects.droplite.repository;

import com.projects.droplite.entity.FileMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    Page<FileMetadata> findByFileNameContaining(String pattern, Pageable pageable);

    Page<FileMetadata> findByFileType(String fileType, Pageable pageable);

    Page<FileMetadata> findByFileTypeAndFileNameContaining(String fileType, String pattern, Pageable pageable);

}
