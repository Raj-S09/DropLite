package com.projects.droplite.controller;

import com.projects.droplite.constant.Constants;
import com.projects.droplite.dto.ApiResponseDto;
import com.projects.droplite.dto.SearchRequestDto;
import com.projects.droplite.service.IFileService;
import com.projects.droplite.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
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
     *
     * @param file to be uploaded via api
     * @return ResponseEntity of uploaded file metadata
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto> uploadFile(@RequestParam("file") MultipartFile file) {
        return ApiResponseDto.generateResponseEntity(
                Constants.SUCCESS_MSG_FILE_UPLOADED_SUCCESSFULLY, fileService.uploadFile(file));
    }

    /**
     *
     * @return ResponseEntity with list of all uploaded files
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto> listFiles(@RequestBody SearchRequestDto request) {
        return ApiResponseDto.generateResponseEntity(
                Constants.SUCCESS_MSG_FILES_FETCHED_SUCCESSFULLY, fileService.listFiles(request));
    }

    /**
     *
     * @param id of uploaded file
     * @return ResponseEntity of downloadable file
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        Resource file = fileService.getFile(id);
        return ResponseEntity.ok().headers(FileUtils.getDownloadFileHeaders(file)).body(file);
    }

    /**
     *
     * @param id of file to be deleted
     * @return ResponseEntity of deleted file
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ApiResponseDto> deleteFile(@PathVariable Long id) {
        return ApiResponseDto.generateResponseEntity(
                Constants.SUCCESS_MSG_FILES_DELETED_SUCCESSFULLY, fileService.deleteFile(id));
    }

}
