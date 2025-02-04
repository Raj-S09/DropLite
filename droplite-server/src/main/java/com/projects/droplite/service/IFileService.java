package com.projects.droplite.service;

import com.projects.droplite.dto.SearchRequestDto;
import com.projects.droplite.entity.FileMetadata;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    FileMetadata uploadFile(MultipartFile file);

    Page<FileMetadata> listFiles(SearchRequestDto request);

    Resource getFile(Long id);

    FileMetadata deleteFile(Long id);

}
