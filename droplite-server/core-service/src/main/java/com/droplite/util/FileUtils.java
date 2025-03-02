package com.droplite.util;

import com.droplite.constant.DropliteConstants;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    private FileUtils() {
        throw new IllegalStateException();
    }

    public static String generateFileName(MultipartFile file) {
        return System.currentTimeMillis() + DropliteConstants.HYPHEN + file.getOriginalFilename();
    }

    public static String getFileType(String filename) {
        if (filename == null || filename.isEmpty()) return DropliteConstants.BLANK;
        int lastIndexOfDot = filename.lastIndexOf(DropliteConstants.DOT);
        return (lastIndexOfDot == -1) ? DropliteConstants.BLANK : filename.substring(lastIndexOfDot + 1);
    }

    public static HttpHeaders getDownloadFileHeaders(Resource file) {
        // Prepare Content-Disposition header for file download
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"");
        // Set content type to application/octet-stream to indicate binary content
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        return headers;
    }

}
