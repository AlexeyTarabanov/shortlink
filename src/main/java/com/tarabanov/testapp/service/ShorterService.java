package com.tarabanov.testapp.service;

import com.tarabanov.testapp.model.Shorter;
import com.tarabanov.testapp.repository.ShorterRepository;
import com.tarabanov.testapp.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.PostConstruct;
import java.net.URLDecoder;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ShorterService {

    private final ShorterRepository shorterRepository;

    @PostConstruct
    void init() {
        log.debug("init: Adding links to table");

        shorterRepository.save(
                Shorter.builder()
                        .hash("1")
                        .createdAt(ZonedDateTime.now())
                        .originalUrl("https://facebook.com")
                        .count(0L)
                        .build()
        );
        log.debug("#init: link saved");

        shorterRepository.save(
                Shorter.builder()
                        .hash("2")
                        .createdAt(ZonedDateTime.now())
                        .originalUrl("https://mail.google.com/mail/u/0/")
                        .count(0L)
                        .build()
        );
        log.debug("#init: link saved");

        shorterRepository.save(
                Shorter.builder()
                        .hash("3")
                        .createdAt(ZonedDateTime.now())
                        .originalUrl("https://instagram.com")
                        .count(0L)
                        .build()
        );
        log.debug("#init: link saved");
    }


    private Integer shorterLength;

    public ShorterService(
            ShorterRepository shorterRepository,
            @Value("${generator.length: 6}") Integer shorterLength) {
        this.shorterRepository = shorterRepository;
        this.shorterLength = shorterLength;
    }

    public Shorter generateShortUrl(Shorter shorter) {

        String hash = CodeGenerator.of(shorterLength).generate();
        if (Objects.nonNull(shorter) && StringUtils.isNotEmpty(shorter.getOriginalUrl())) {
            String shorterString = URLDecoder.decode(shorter.getOriginalUrl());
            shorter = new Shorter(null, hash, shorterString, ZonedDateTime.now());
            return shorterRepository.save(shorter);
        } else {
            return null;
        }
    }

    public ResponseEntity<String> redirectShorterUrl(@PathVariable("hash") String hash) {

        Shorter shorter = shorterRepository.findByHash(hash);
        if (Objects.nonNull(shorter)) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, shorter.getOriginalUrl());

            Long count = shorter.getCount();
            if (count != null) {
                count++;
                shorter.setCount(count);
                shorterRepository.save(shorter);
            } else {
                shorter.setCount(1L);
                shorterRepository.save(shorter);
            }

            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public void deleteById(Long id) {
        shorterRepository.deleteById(id);
    }

    public List<Shorter> findAll() {
        return shorterRepository.findAll();
    }
}
