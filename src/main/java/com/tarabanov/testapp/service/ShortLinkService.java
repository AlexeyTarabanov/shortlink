package com.tarabanov.testapp.service;

import com.tarabanov.testapp.model.ShortLink;
import com.tarabanov.testapp.repository.LinkRepository;
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
@RequiredArgsConstructor
public class ShortLinkService {

    private final LinkRepository linkRepository;
    private final CodeGenerator codeGenerator;

    @PostConstruct
    void init() {
        log.debug("init: Adding links to table");

        linkRepository.save(
                ShortLink.builder()
                        .hash("1")
                        .createdAt(ZonedDateTime.now())
                        .originalUrl("https://facebook.com")
                        .count(0L)
                        .build()
        );
        log.debug("#init: link saved");

        linkRepository.save(
                ShortLink.builder()
                        .hash("2")
                        .createdAt(ZonedDateTime.now())
                        .originalUrl("https://mail.google.com/mail/u/0/")
                        .count(0L)
                        .build()
        );
        log.debug("#init: link saved");

        linkRepository.save(
                ShortLink.builder()
                        .hash("3")
                        .createdAt(ZonedDateTime.now())
                        .originalUrl("https://instagram.com")
                        .count(0L)
                        .build()
        );
        log.debug("#init: link saved");
    }

    public ShortLink generateShortUrl(ShortLink shortLink) {

        String hash = codeGenerator.generate();

        if (Objects.nonNull(shortLink) && StringUtils.isNotEmpty(shortLink.getOriginalUrl())) {
            String shorterString = URLDecoder.decode(shortLink.getOriginalUrl());
            shortLink = new ShortLink(null, hash, shorterString, ZonedDateTime.now());
            return linkRepository.save(shortLink);
        } else {
            return null;
        }
    }

    public ResponseEntity<String> redirectShorterUrl(String hash) {

        ShortLink shortLink = linkRepository.findByHash(hash); // вренуть мок с коунт = 0
        if (Objects.nonNull(shortLink)) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, shortLink.getOriginalUrl());

            Long count = shortLink.getCount();
            if (count != null) {
                count++;
                shortLink.setCount(count);
                linkRepository.save(shortLink);
            } else {
                shortLink.setCount(1L);
                linkRepository.save(shortLink);
            }

            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public void deleteById(Long id) {
        linkRepository.deleteById(id);
    }

    public List<ShortLink> findAll() {
        return linkRepository.findAll();
    }
}
