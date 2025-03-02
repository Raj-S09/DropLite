package com.droplite.validator.impl;

import com.droplite.constant.FileConstants;
import com.droplite.dto.SearchRequestDto;
import com.droplite.exception.DropLiteException;
import com.droplite.util.FileUtils;
import com.droplite.validator.IFileValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileValidatorImpl implements IFileValidator {

    @Value("${file.upload.max-size}")
    private Integer fileUploadSizeLimit;

    @Value("#{'${file.upload.allowed-types}'.split(',')}")
    private String[] allowedFileTypes;

    /**
     * Throws error if file is not valid
     * @param file to validate
     */
    public void validateFile(MultipartFile file) {
        // Validate file type
        if (!List.of(allowedFileTypes).contains(FileUtils.getFileType(file.getOriginalFilename())))
            throw new DropLiteException(FileConstants.ERROR_MSG_FILE_NOT_SUPPORTED);
        // Validate file size
        if (file.getSize() > fileUploadSizeLimit * 1024 * 1024)
            throw new DropLiteException(String.format(FileConstants.ERROR_MSG_FILE_EXCEEDS_SIZE_LIMIT, fileUploadSizeLimit));
    }

    /**
     * Throws error if search request is malformed
     * @param requestDto to validate
     */
    public void validateSearchRequest(SearchRequestDto requestDto) {
        // Validate page size
        if (requestDto.getPageSize() > 50)
            throw new DropLiteException(FileConstants.ERROR_MSG_PAGE_SIZE_TOO_MUCH);
    }
}
