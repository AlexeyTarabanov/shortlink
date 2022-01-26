package com.tarabanov.testapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(schema = "shorter")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
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
}
