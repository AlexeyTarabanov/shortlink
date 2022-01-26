package com.tarabanov.testapp.model;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicLong;

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
    // columnDefinition (Необязательно) Фрагмент SQL, используемый при создании DDL для столбца.
    // По умолчанию используется сгенерированный SQL для создания столбца предполагаемого типа.
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
