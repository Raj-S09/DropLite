package com.droplite.repository;

import com.droplite.entity.FileDownloadToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface FileDownloadTokenRepository extends JpaRepository<FileDownloadToken, Long> {

    Optional<FileDownloadToken> findByFileIdAndTokenAndExpireAtBefore(Long fileId, String token, Date now);

    Optional<FileDownloadToken> findByFileId(Long fileId);

}
