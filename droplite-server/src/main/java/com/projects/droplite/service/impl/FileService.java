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

    public FileMetadata uploadFile(MultipartFile file) {
        try {
            // Ensure directory exists
            Files.createDirectories(Path.of(fileStoragePath));

            // Save file locally
            String fileName = FileUtils.generateFileName(file);
            Path targetPath = Path.of(fileStoragePath).toAbsolutePath().resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Save metadata to DB
            FileMetadata metadata = FileMetadata.builder()
                    .fileName(file.getOriginalFilename())
                    .fileType(FileUtils.getFileExtension(file.getOriginalFilename()))
                    .filePath(targetPath.toString())
                    .uploadedAt(LocalDateTime.now())
                    .build();
            return fileMetadataRepository.save(metadata);

        } catch (Exception ex) {
            throw new DropLiteException(Constants.ERROR_MSG_FILE_UPLOAD_FAILED);
        }
    }

    public List<FileMetadata> getAllFiles() {
        return fileMetadataRepository.findAll();
    }

    public FileSystemResource getFile(Long id) {
        return new FileSystemResource(
                Paths.get(fileMetadataRepository.findById(id)
                        .orElseThrow(() -> new DropLiteException(Constants.ERROR_MSG_FILE_NOT_FOUND))
                        .getFilePath()));
    }
}
