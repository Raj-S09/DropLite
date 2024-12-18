package com.projects.droplite.controller;

import com.projects.droplite.constant.Constants;
import com.projects.droplite.dto.ApiResponseDto;
import com.projects.droplite.entity.FileMetadata;
import com.projects.droplite.service.IFileService;
import com.projects.droplite.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final IFileService fileService;

    /**
     *
     * @param file to be uploaded via api
     * @return ResponseEntity of uploaded file metadata
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto<FileMetadata>> uploadFile(@RequestParam("file") MultipartFile file) {
        return ApiResponseDto.generateResponseEntity(
                Constants.SUCCESS_MSG_FILE_UPLOADED_SUCCESSFULLY, fileService.uploadFile(file));
    }

    /**
     *
     * @return ResponseEntity with list of all uploaded files
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<FileMetadata>>> listFiles() {
        return ApiResponseDto.generateResponseEntity(
                Constants.SUCCESS_MSG_FILES_FETCHED_SUCCESSFULLY, fileService.getAllFiles());
    }

    /**
     *
     * @param id of uploaded file
     * @return ResponseEntity of downloadable file
     */
    @GetMapping("/{id}")
    public ResponseEntity<FileSystemResource> downloadFile(@PathVariable Long id) {
        FileSystemResource file = fileService.getFile(id);
        return ResponseEntity.ok().headers(FileUtils.getDownloadFileHeaders(file)).body(file);
    }
}
