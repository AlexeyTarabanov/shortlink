package com.tarabanov.testapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
public class ShortLink {
    @Id
    @GeneratedValue()
    private Long id;
    @Column()
    private String hash;
    @Column()
    private String originalUrl;
    @Column()
    private ZonedDateTime createdAt;
    @Column()
    private Long count;

    public ShortLink(Long id, String hash, String originalUrl, ZonedDateTime createdAt) {
        this.id = id;
        this.hash = hash;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
    }
}
