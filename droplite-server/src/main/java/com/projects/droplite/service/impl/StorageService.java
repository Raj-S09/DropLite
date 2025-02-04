package com.projects.droplite.service.impl;

import com.projects.droplite.constant.Constants;
import com.projects.droplite.service.IStorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

@Slf4j
@Service
public class StorageService implements IStorageService {

    private S3Client s3;

    @Value("${s3.bucket.name}")
    private String bucketName;

    @Value("${s3.region}")
    private String region;

    @Value("${s3.endpoint}")
    private String s3Endpoint;

    @Value("${s3.username}")
    private String s3Username;

    @Value("${s3.password}")
    private String s3Password;

    private static final String UPLOAD_DIRECTORY = "uploads";

    @PostConstruct
    public void init() {
        s3 = S3Client.builder()
                .endpointOverride(URI.create(s3Endpoint))
                .region(Region.AWS_GLOBAL) // MinIO ignores region
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(s3Username, s3Password)))
                .forcePathStyle(true)
                .build();
    }

    // Uploads file
    public String uploadFile(Resource resource) {
        String key = UPLOAD_DIRECTORY + Constants.SLASH + resource.getFilename();
        log.debug("Uploading file: {}", key);
        try {
            s3.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(),
                    RequestBody.fromInputStream(resource.getInputStream(), resource.contentLength())
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        log.debug("File uploaded: {}", key);
        return key;
    }

    // Download a file
    public Resource downloadFile(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        InputStream getResponse = s3.getObject(request);
        log.debug("File downloaded: {}", key);
        return new InputStreamResource(getResponse);
    }

    // List all files
    public List<String> listFiles() {
        return s3.listObjectsV2(ListObjectsV2Request.builder()
                        .bucket(bucketName)
                        .build())
                .contents()
                .stream()
                .map(S3Object::key)
                .toList();
    }

    // Delete a file
    public void deleteFile(String key) {
        s3.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
        log.debug("File deleted: {}", key);
    }

}
