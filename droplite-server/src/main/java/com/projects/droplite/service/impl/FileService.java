package com.projects.droplite.service.impl;

import com.projects.droplite.constant.Constants;
import com.projects.droplite.entity.FileMetadata;
import com.projects.droplite.exception.DropLiteException;
import com.projects.droplite.repository.FileMetadataRepository;
import com.projects.droplite.service.IFileService;
import com.projects.droplite.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService implements IFileService {

    private final FileMetadataRepository fileMetadataRepository;

    @Value("${file.upload.dir}")
    private String fileStoragePath;

    @Value("${file.upload.max-size}")
    private Integer fileUploadSizeLimit;

    @Value("#{'${file.upload.allowed-types}'.split(',')}")
    private String[] allowedFileTypes;

    /**
     *
     * @param file to be uploaded via api
     * @return FileMetadata object after uploading the file
     */
    public FileMetadata uploadFile(MultipartFile file) {
        // Validate file
        validateFile(file);
        try {
            // Upload file locally and get path
            String filePath = getUploadedFilePath(file);
            // Save metadata to DB and return it
            return saveFileMetadataToDB(file, filePath);
        } catch (Exception ex) {
            throw new DropLiteException(Constants.ERROR_MSG_FILE_UPLOAD_FAILED);
        }
    }

    /**
     *
     * @return List of all uploaded files
     */
    public List<FileMetadata> getAllFiles() {
        return fileMetadataRepository.findAll();
    }

    /**
     *
     * @param id of the uploaded file
     * @return File corresponding to given id
     */
    public FileSystemResource getFile(Long id) {
        return new FileSystemResource(Paths.get(fileMetadataRepository
                .findById(id)
                .orElseThrow(() -> new DropLiteException(Constants.ERROR_MSG_FILE_NOT_FOUND))
                .getFilePath()));
    }

    /**
     * Throws error if file is not valid
     * @param file to validate
     */
    private void validateFile(MultipartFile file) {
        // Validate file type
        if (!List.of(allowedFileTypes).contains(FileUtils.getFileType(file.getOriginalFilename())))
            throw new DropLiteException(Constants.ERROR_MSG_FILE_NOT_SUPPORTED);
        // Validate file size
        if (file.getSize() > fileUploadSizeLimit * 1024 * 1024)
            throw new DropLiteException(String.format(Constants.ERROR_MSG_FILE_EXCEEDS_SIZE_LIMIT, fileUploadSizeLimit));
    }

    /**
     *
     * @param file to upload
     * @return uploads file to local system and returns its path
     * @throws IOException if upload fails
     */
    private String getUploadedFilePath(MultipartFile file) throws IOException {
        // Ensure directory exists
        Files.createDirectories(Path.of(fileStoragePath));

        // Save file locally
        String fileName = FileUtils.generateFileName(file);
        Path targetPath = Path.of(fileStoragePath).toAbsolutePath().resolve(fileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return targetPath.toString();
    }

    /**
     *
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
