package com.projects.droplite.validator;

import com.projects.droplite.dto.SearchRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface IFileValidator {

    void validateFile(MultipartFile file);
    void validateSearchRequest(SearchRequestDto requestDto);
}
