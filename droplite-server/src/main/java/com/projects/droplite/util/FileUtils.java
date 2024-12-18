package com.projects.droplite.util;

import com.projects.droplite.constant.Constants;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    private FileUtils() {
        throw new IllegalStateException();
    }

    public static String generateFileName(MultipartFile file) {
        return System.currentTimeMillis() + Constants.HYPHEN + file.getOriginalFilename();
    }

    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) return Constants.BLANK;
        int lastIndexOfDot = filename.lastIndexOf(Constants.DOT);
        return (lastIndexOfDot == -1) ? Constants.BLANK : filename.substring(lastIndexOfDot + 1);
    }

    public static HttpHeaders getDownloadFileHeaders(FileSystemResource file) {
        // Prepare Content-Disposition header for file download
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"");
        // Set content type to application/octet-stream to indicate binary content
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        return headers;
    }
}
