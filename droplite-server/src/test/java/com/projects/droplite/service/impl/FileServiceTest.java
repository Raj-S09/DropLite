package com.projects.droplite.service.impl;

import com.projects.droplite.constant.Constants;
import com.projects.droplite.entity.FileMetadata;
import com.projects.droplite.exception.DropLiteException;
import com.projects.droplite.repository.FileMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FileServiceTest {

    @Mock
    private FileMetadataRepository fileMetadataRepository;

    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp() {
        // Initialize mocks

        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(fileService, "fileStoragePath", ".");
        ReflectionTestUtils.setField(fileService, "fileUploadSizeLimit", 5);
        ReflectionTestUtils.setField(fileService, "allowedFileTypes", new String[]{"txt"});
    }

    @Test
    void testUploadFile_ShouldThrowException_WhenFileExceedsSizeLimit() {
        // Arrange
        when(mockFile.getSize()).thenReturn(1024L * 1024 * 6); // 6 MB
        when(mockFile.getOriginalFilename()).thenReturn("test.txt");


        // Act & Assert
        DropLiteException exception = assertThrows(DropLiteException.class, () -> fileService.uploadFile(mockFile));
        assertEquals(String.format(Constants.ERROR_MSG_FILE_EXCEEDS_SIZE_LIMIT, 5), exception.getMessage());
    }

    @Test
    void testUploadFile_ShouldThrowException_WhenFileIsNotOfValidFormat() {
        // Arrange
        when(mockFile.getSize()).thenReturn(1024L * 1024 * 4); // 6 MB
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");

        // Act & Assert
        DropLiteException exception = assertThrows(DropLiteException.class, () -> fileService.uploadFile(mockFile));
        assertEquals(Constants.ERROR_MSG_FILE_NOT_SUPPORTED, exception.getMessage());
    }

    @Test
    void testGetAllFiles_ShouldReturnListOfFiles() {
        // Arrange
        when(fileMetadataRepository.findAll()).thenReturn(List.of(new FileMetadata(), new FileMetadata()));

        // Act
        List<FileMetadata> files = fileService.getAllFiles();

        // Assert
        assertEquals(2, files.size());
    }

    @Test
    void testGetFile_ShouldReturnFile_WhenFileExists() {
        // Arrange
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setFilePath("/uploads/test.txt");
        when(fileMetadataRepository.findById(1L)).thenReturn(Optional.of(fileMetadata));

        // Act
        FileSystemResource fileResource = fileService.getFile(1L);

        // Assert
        assertNotNull(fileResource);
        assertEquals("/uploads/test.txt", fileResource.getPath());
    }

    @Test
    void testGetFile_ShouldThrowException_WhenFileNotFound() {
        // Arrange
        when(fileMetadataRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        DropLiteException exception = assertThrows(DropLiteException.class, () -> {
            fileService.getFile(1L);
        });
        assertEquals(Constants.ERROR_MSG_FILE_NOT_FOUND, exception.getMessage());
    }
}