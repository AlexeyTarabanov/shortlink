package com.tarabanov.testapp.controller;

import com.tarabanov.testapp.model.Shorter;
import com.tarabanov.testapp.repository.ShorterRepository;
import com.tarabanov.testapp.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.time.ZonedDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;



@RestController
@RequestMapping
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
        // сгенерировать хэш исходного URL
        // и вернуть его

        String hash = codeGenerator.generate(shorterLength);
        if (shorter != null) {
            String shorterString = URLDecoder.decode(shorter.getOriginalUrl());
            shorter = new Shorter(null, hash, shorterString, ZonedDateTime.now());
            return repository.save(shorter);
        } else {
            return null;
        }
    }

    // при переходе на нашу короткую ссылку мы должны перенаправлять пользователя на оригинальную ссылку
    @GetMapping("/{hash}")
    public ResponseEntity redirectShorter(@PathVariable("hash") String hash) {
        // найти хэш в БД и перенаправить на исходный URL
        Shorter shorter = repository.findByHash(hash);
        if (shorter != null) {
            //если мы нашли код, то делаем редирект на ссылку
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", shorter.getOriginalUrl());
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        } else {
            //не нашли, выкидываем ошибку не найден 404
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}
