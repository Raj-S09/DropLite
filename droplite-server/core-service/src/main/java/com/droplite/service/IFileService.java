package com.droplite.service;

import com.droplite.dto.FileDto;
import com.droplite.dto.SearchRequestDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    FileDto uploadFile(MultipartFile file);

    Page<FileDto> listFiles(SearchRequestDto request);

    Resource getFile(Long id);

    FileDto deleteFile(Long id);

}
