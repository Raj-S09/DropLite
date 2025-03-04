package com.droplite.controller;

import com.droplite.service.IFileService;
import com.droplite.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final IFileService fileService;

    /**
     * @param fileId of uploaded file
     * @return ResponseEntity of downloadable file
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam Long fileId, @RequestParam String token) {
        Resource file = fileService.getFile(fileId, token);
        return ResponseEntity.ok().headers(FileUtils.getDownloadFileHeaders(file)).body(file);
    }

}
