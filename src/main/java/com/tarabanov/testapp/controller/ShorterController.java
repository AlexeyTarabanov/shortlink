package com.tarabanov.testapp.controller;

import com.tarabanov.testapp.model.Shorter;
import com.tarabanov.testapp.service.ShorterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/short-link")
@RequiredArgsConstructor
public class ShorterController {

    private final ShorterService shorterService;

    // сохранение оригинальной ссылки и генерация короткого кода 'hash'
    @PostMapping(path = "/")
    public Shorter createShortUrl(@RequestBody Shorter shorter) {
        return shorterService.generateShortUrl(shorter);
    }

    // при переходе на нашу короткую ссылку перенаправляет пользователя на оригинальную ссылку
    @GetMapping("/{hash}")
    public ResponseEntity<String> redirectShorter(@PathVariable("hash") String hash) {
        return shorterService.redirectShorterUrl(hash);
    }

    @DeleteMapping ("link-delete/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable("id") Long id) {
        return shorterService.deleteById(id);
    }

    @GetMapping("/all-link")
    public ResponseEntity<Iterable<Shorter>> findAll() {
        return shorterService.findAll();
    }
}
