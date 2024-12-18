package com.projects.droplite.service;

import com.projects.droplite.entity.FileMetadata;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {

    FileMetadata uploadFile(MultipartFile file);

    List<FileMetadata> getAllFiles();

    FileSystemResource getFile(Long id);
}
