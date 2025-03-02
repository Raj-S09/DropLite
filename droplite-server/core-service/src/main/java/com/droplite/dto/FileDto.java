package com.droplite.dto;

import com.droplite.constant.DropliteConstants;
import com.droplite.entity.FileMetadata;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class FileDto {

    private Long id;
    private String fileName;
    private String fileType;

    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DropliteConstants.DEFAULT_DATE_FORMAT, timezone = DropliteConstants.DEFAULT_TIME_ZONE)
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DropliteConstants.DEFAULT_DATE_FORMAT, timezone = DropliteConstants.DEFAULT_TIME_ZONE)
    private Date updatedAt;

    public FileDto(FileMetadata fileMetadata) {
        id = fileMetadata.getId();
        fileName = fileMetadata.getFileName();
        fileType = fileMetadata.getFileType();
        createdBy = fileMetadata.getCreatedBy();
        createdAt = fileMetadata.getCreatedAt();
        updatedAt = fileMetadata.getUpdatedAt();
    }
}
