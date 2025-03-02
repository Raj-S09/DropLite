package com.droplite.validator;

import com.droplite.dto.SearchRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface IFileValidator {

    void validateFile(MultipartFile file);
    void validateSearchRequest(SearchRequestDto requestDto);
}
