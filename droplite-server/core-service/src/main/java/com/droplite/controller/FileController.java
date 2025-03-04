package com.droplite.controller;

import com.droplite.constant.FileConstants;
import com.droplite.dto.ApiResponseDto;
import com.droplite.dto.FileDto;
import com.droplite.dto.SearchRequestDto;
import com.droplite.service.IFileService;
import com.droplite.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final IFileService fileService;

    /**
     * @param file to be uploaded via api
     * @return ResponseEntity of uploaded file metadata
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String userId) {
        return ApiResponseDto.generateResponseEntity(
                FileConstants.SUCCESS_MSG_FILE_UPLOADED_SUCCESSFULLY, fileService.uploadFile(file, userId));
    }

    /**
     * @return ResponseEntity with list of all uploaded files
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto> listFiles(@RequestBody SearchRequestDto request) {
        Page<FileDto> pagedResponse = fileService.listFiles(request);
        return ApiResponseDto.generateResponseEntity(
                FileConstants.SUCCESS_MSG_FILES_FETCHED_SUCCESSFULLY,
                pagedResponse.getContent(), pagedResponse.getTotalPages(), pagedResponse.getTotalElements());
    }

    /**
     * @param id of uploaded file
     * @return ResponseEntity of downloadable file
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        Resource file = fileService.getFile(id);
        return ResponseEntity.ok().headers(FileUtils.getDownloadFileHeaders(file)).body(file);
    }

    /**
     * @param id of uploaded file
     * @return ResponseEntity of downloadable file
     */
    @GetMapping("/share")
    public ResponseEntity<Resource> downloadFile(@RequestParam Long id, @RequestParam String token) {
        Resource file = fileService.getFile(id, token);
        return ResponseEntity.ok().headers(FileUtils.getDownloadFileHeaders(file)).body(file);
    }

    /**
     * @param id of file to be deleted
     * @return ResponseEntity of deleted file
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deleteFile(@PathVariable Long id) {
        return ApiResponseDto.generateResponseEntity(
                FileConstants.SUCCESS_MSG_FILES_DELETED_SUCCESSFULLY, fileService.deleteFile(id));
    }

}
