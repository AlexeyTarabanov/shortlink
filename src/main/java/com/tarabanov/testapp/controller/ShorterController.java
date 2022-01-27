package com.tarabanov.testapp.controller;

import com.tarabanov.testapp.model.Shorter;
import com.tarabanov.testapp.repository.ShorterRepository;
import com.tarabanov.testapp.util.CodeGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.time.ZonedDateTime;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;



@RestController
@RequestMapping("/short-link")
public class ShorterController {

    private final ShorterRepository repository;

    private final CodeGenerator codeGenerator;
    @Value("${shorter.length}")
    private Integer shorterLength;

    @Autowired
    public ShorterController(ShorterRepository repository) {
        this.repository = repository;
        this.codeGenerator = new CodeGenerator();
    }

    // сохранение оригинальной ссылки и генерация короткого кода 'hash'
    @PostMapping(path = "/", consumes = APPLICATION_JSON_VALUE)
    public Shorter createShortUrl(@RequestBody Shorter shorter) {

        String hash = codeGenerator.generate(shorterLength);
        if (Objects.nonNull(shorter) && StringUtils.isNotEmpty(shorter.getOriginalUrl())) {
            String shorterString = URLDecoder.decode(shorter.getOriginalUrl());
            shorter = new Shorter(null, hash, shorterString, ZonedDateTime.now());
            return repository.save(shorter);
        } else {
            return null;
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        repository.deleteShorterById(id);
        return ResponseEntity.ok().build();
    }

    // при переходе на нашу короткую ссылку перенаправляет пользователя на оригинальную ссылку
    @GetMapping("/{hash}")
    public ResponseEntity<String> redirectShorter(@PathVariable("hash") String hash) {
        // найти хэш в БД и перенаправить на исходный URL
        Shorter shorter = repository.findByHash(hash);
        if (Objects.nonNull(shorter)) {
            //если мы нашли код, то делаем редирект на ссылку
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, shorter.getOriginalUrl());

            Long count = shorter.getCount();
            if (count != null) {
                count++;
                shorter.setCount(count);
                System.out.println(count);
                repository.save(shorter);
            } else {
                shorter.setCount(1L);
                repository.save(shorter);
            }

            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            //не нашли, выкидываем ошибку не найден 404
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Shorter>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}
