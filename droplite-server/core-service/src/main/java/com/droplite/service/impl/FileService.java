package com.droplite.service.impl;

import com.droplite.constant.FileConstants;
import com.droplite.dto.FileDto;
import com.droplite.dto.SearchRequestDto;
import com.droplite.entity.FileDownloadToken;
import com.droplite.entity.FileMetadata;
import com.droplite.exception.DropLiteException;
import com.droplite.repository.FileDownloadTokenRepository;
import com.droplite.repository.FileMetadataRepository;
import com.droplite.service.IFileService;
import com.droplite.service.IStorageService;
import com.droplite.util.FileUtils;
import com.droplite.util.SecurityUtils;
import com.droplite.validator.IFileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService implements IFileService {

    private final FileMetadataRepository fileMetadataRepository;
    private final FileDownloadTokenRepository fileDownloadTokenRepository;
    private final IFileValidator validator;
    private final IStorageService storageService;

    /**
     * @param file to be uploaded via api
     * @return FileMetadata object after uploading the file
     */
    @Override
    public FileDto uploadFile(MultipartFile file, String userId) {
        // Validate file
        validator.validateFile(file);
        try {
            // Upload file locally and get path
            String filePath = storageService.uploadFile(file.getResource());
            // Save metadata to DB and return it
            return saveFileMetadataToDB(file, filePath, userId);
        } catch (Exception e) {
            throw new DropLiteException(FileConstants.ERROR_MSG_FILE_UPLOAD_FAILED, e);
        }
    }

    /**
     * @param request as the filter
     * @return List of all filtered uploaded files
     */
    @Override
    public Page<FileDto> listFiles(SearchRequestDto request) {
        // Validate request
        validator.validateSearchRequest(request);
        Sort sort = Sort.by(Sort.Direction.valueOf(request.getSortOrder()), request.getSortField());
        Pageable pageable = PageRequest.of(request.getPageNumber() - 1, request.getPageSize(), sort);
        if (StringUtils.hasText(request.getPattern()) && StringUtils.hasText(request.getFileType()))
            return fileMetadataRepository.findByFileTypeAndFileNameContaining(request.getFileType(), request.getPattern(), pageable)
                    .map(FileDto::new);
        if (StringUtils.hasText(request.getPattern()))
            return fileMetadataRepository.findByFileNameContaining(request.getPattern(), pageable)
                    .map(FileDto::new);
        if (StringUtils.hasText(request.getFileType()))
            return fileMetadataRepository.findByFileType(request.getFileType(), pageable)
                    .map(FileDto::new);
        return fileMetadataRepository.findAll(pageable).map(FileDto::new);
    }

    /**
     * @param id of the uploaded file
     * @return File corresponding to given id
     */
    @Override
    public Resource getFile(Long id) {
        Optional<FileMetadata> metadata = fileMetadataRepository.findById(id);
        if (metadata.isEmpty()) throw new DropLiteException(FileConstants.ERROR_MSG_FILE_NOT_FOUND);
        return storageService.downloadFile(metadata.get().getFilePath());
    }

    /**
     * @param id of the uploaded file
     * @param token generated for shareable file
     * @return File corresponding to given id
     */
    @Override
    public Resource getFile(Long id, String token) {
        Optional<FileDownloadToken> downloadToken = fileDownloadTokenRepository
                .findByFileIdAndTokenAndExpireAtBefore(id, token, new Date());
        if (downloadToken.isEmpty())
            throw new DropLiteException(FileConstants.ERROR_MSG_FILE_DOWNLOAD_FAILED);
        return getFile(id);
    }

    /**
     * @param id of the file to delete
     * @return deletes file and returns its path
     */
    @Override
    public FileDto deleteFile(Long id) {
        Optional<FileMetadata> fileMetadata = fileMetadataRepository.findById(id);
        if (fileMetadata.isEmpty()) {
            throw new DropLiteException(FileConstants.ERROR_MSG_FILE_NOT_FOUND);
        }
        fileMetadataRepository.deleteById(id);
        String key = fileMetadata.get().getFilePath();
        try {
            storageService.deleteFile(key);
        } catch (Exception e) {
            throw new DropLiteException(FileConstants.ERROR_MSG_FILE_DELETE_FAILED, e);
        }
        return new FileDto(fileMetadata.get());
    }

    @Override
    public String createShareableLink(Long id) {
        Optional<FileDownloadToken> downloadToken = fileDownloadTokenRepository.findByFileId(id);
        if (downloadToken.isEmpty()) {
            FileDownloadToken newToken = FileDownloadToken.builder()
                    .fileId(id)
                    .token(SecurityUtils.generateToken())
                    .build();
            return fileDownloadTokenRepository.save(newToken).getToken();
        }
        return downloadToken.get().getToken();
    }

    /**
     * @param file to save
     * @param path to save in filePath field
     * @return FileMetadata object saved in DB
     */
    private FileDto saveFileMetadataToDB(MultipartFile file, String path, String userId) {
        FileMetadata metadata = FileMetadata.builder()
                .fileName(file.getOriginalFilename())
                .fileType(FileUtils.getFileType(file.getOriginalFilename()))
                .createdBy(userId)
                .filePath(path)
                .build();
        FileMetadata savedFileMetadata = fileMetadataRepository.save(metadata);
        return new FileDto(savedFileMetadata);
    }

}
