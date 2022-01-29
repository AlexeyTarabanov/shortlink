package com.tarabanov.testapp.service;

import com.tarabanov.testapp.model.Shorter;
import com.tarabanov.testapp.repository.ShorterRepository;
import com.tarabanov.testapp.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URLDecoder;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ShorterService {

    private final ShorterRepository shorterRepository;

    @Value("6")
    private Integer shorterLength;

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
