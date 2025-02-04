package com.projects.droplite.service.impl;

import com.projects.droplite.constant.Constants;
import com.projects.droplite.dto.SearchRequestDto;
import com.projects.droplite.entity.FileMetadata;
import com.projects.droplite.exception.DropLiteException;
import com.projects.droplite.repository.FileMetadataRepository;
import com.projects.droplite.service.IFileService;
import com.projects.droplite.service.IStorageService;
import com.projects.droplite.util.FileUtils;
import com.projects.droplite.validator.IFileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService implements IFileService {

    private final FileMetadataRepository fileMetadataRepository;
    private final IFileValidator validator;
    private final IStorageService storageService;

    @Value("${file.upload.dir}")
    private String fileStoragePath;

    /**
     * @param file to be uploaded via api
     * @return FileMetadata object after uploading the file
     */
    public FileMetadata uploadFile(MultipartFile file) {
        // Validate file
        validator.validateFile(file);
        try {
            // Upload file locally and get path
            String filePath = storageService.uploadFile(file.getResource());
            // Save metadata to DB and return it
            return saveFileMetadataToDB(file, filePath);
        } catch (Exception e) {
            throw new DropLiteException(Constants.ERROR_MSG_FILE_UPLOAD_FAILED, e);
        }
    }

    /**
     * @param request as the filter
     * @return List of all filtered uploaded files
     */
    public Page<FileMetadata> listFiles(SearchRequestDto request) {
        // Validate request
        validator.validateSearchRequest(request);
        Sort sort = Sort.by(Sort.Direction.valueOf(request.getSortOrder()), request.getSortField());
        Pageable pageable = PageRequest.of(request.getPageNumber() - 1, request.getPageSize(), sort);
        if (StringUtils.hasText(request.getPattern()) && StringUtils.hasText(request.getFileType()))
            return fileMetadataRepository.findByFileTypeAndFileNameContaining(
                    request.getFileType(), request.getPattern(), pageable);
        if (StringUtils.hasText(request.getPattern()))
            return fileMetadataRepository.findByFileNameContaining(
                    request.getPattern(), pageable);
        if (StringUtils.hasText(request.getFileType()))
            return fileMetadataRepository.findByFileType(
                    request.getFileType(), pageable);
        return fileMetadataRepository.findAll(pageable);
    }

    /**
     * @param id of the uploaded file
     * @return File corresponding to given id
     */
    public Resource getFile(Long id) {
        Optional<FileMetadata> metadata = fileMetadataRepository.findById(id);
        if (metadata.isEmpty()) throw new DropLiteException(Constants.ERROR_MSG_FILE_NOT_FOUND);
        return storageService.downloadFile(metadata.get().getFilePath());
    }

    /**
     * @param id of the file to delete
     * @return deletes file and returns its path
     */
    @Override
    public FileMetadata deleteFile(Long id) {
        Optional<FileMetadata> fileMetadata = fileMetadataRepository.findById(id);
        if (fileMetadata.isEmpty()) {
            throw new DropLiteException(Constants.ERROR_MSG_FILE_NOT_FOUND);
        }
        fileMetadataRepository.deleteById(id);
        String key = fileMetadata.get().getFilePath();
        try {
            storageService.deleteFile(key);
        } catch (Exception e) {
            throw new DropLiteException(Constants.ERROR_MSG_FILE_DELETE_FAILED, e);
        }
        return fileMetadata.get();
    }

    /**
     * @param file to save
     * @param path to save in filePath field
     * @return FileMetadata object saved in DB
     */
    private FileMetadata saveFileMetadataToDB(MultipartFile file, String path) {
        FileMetadata metadata = FileMetadata.builder()
                .fileName(file.getOriginalFilename())
                .fileType(FileUtils.getFileType(file.getOriginalFilename()))
                .filePath(path)
                .uploadedAt(LocalDateTime.now())
                .build();
        return fileMetadataRepository.save(metadata);
    }

}
