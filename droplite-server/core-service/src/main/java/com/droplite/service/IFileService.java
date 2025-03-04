package com.droplite.service;

import com.droplite.dto.FileDto;
import com.droplite.dto.SearchRequestDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    FileDto uploadFile(MultipartFile file, String userId);

    Page<FileDto> listFiles(SearchRequestDto request);

    Resource getFile(Long id);

    Resource getFile(Long id, String token);

    FileDto deleteFile(Long id);

    String createShareableLink(Long id);
}
