package com.tarabanov.testapp.controller;

import com.tarabanov.testapp.model.Shorter;
import com.tarabanov.testapp.service.ShorterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
@Controller
@RequestMapping("/short-link")
@RequiredArgsConstructor
public class ShorterController {

    private final ShorterService shorterService;

    @GetMapping("/shorter-create")
    public String createShorterForm(Shorter shorter) {
        return "shorter-create";
    }

    // сохранение оригинальной ссылки и генерация короткого кода 'hash'
    @PostMapping(path = "/")
    @ResponseBody
    public Shorter createShortUrl(Shorter shorter) {
        return shorterService.generateShortUrl(shorter);
    }

    // при переходе на нашу короткую ссылку перенаправляет пользователя на оригинальную ссылку
    @GetMapping("/{hash}")
    public ResponseEntity<String> redirectShorter(@PathVariable("hash") String hash) {
        return shorterService.redirectShorterUrl(hash);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        shorterService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all-link")
    public String findAll(Model model) {
        List<Shorter> shorts = shorterService.getAll();
        model.addAttribute("shorts", shorts);
        return "link-list";
    }
}
