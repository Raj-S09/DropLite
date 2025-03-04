package com.droplite.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDownloadToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long fileId;

    @Column(nullable = false)
    private String token;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @Column(nullable = false)
    private Date expireAt;

    @PrePersist
    protected void onCreate() {
        this.expireAt = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
    }

}