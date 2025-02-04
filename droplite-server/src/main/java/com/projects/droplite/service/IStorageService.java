package com.projects.droplite.service;

import org.springframework.core.io.Resource;

import java.util.List;

public interface IStorageService {

    // Uploads file
    String uploadFile(Resource resource);

    // Download a file
    Resource downloadFile(String key);

    // List all files
    List<String> listFiles();

    // Delete a file
    void deleteFile(String key);
}
