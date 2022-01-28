package com.tarabanov.testapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(schema = "shorter")
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Shorter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String hash;
    @Column(name = "original_url")
    private String originalUrl;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private ZonedDateTime createdAt;
    @Column(name = "counter")
    private Long count;

    public Shorter(Long id, String hash, String originalUrl, ZonedDateTime createdAt) {
        this.id = id;
        this.hash = hash;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
    }
}
