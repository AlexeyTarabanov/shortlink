package com.tarabanov.testapp.controller;

import com.tarabanov.testapp.model.Shorter;
import com.tarabanov.testapp.service.ShorterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


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
    public String createShortUrl(Shorter shorter, Model model) {
        Shorter shortUrl = shorterService.generateShortUrl(shorter);
        model.addAttribute("shortUrl", shortUrl);
        return "new-link";
    }

    // при переходе на нашу короткую ссылку перенаправляет пользователя на оригинальную ссылку
    @GetMapping("/{hash}")
    public ResponseEntity<String> redirectShorter(@PathVariable("hash") String hash) {
        return shorterService.redirectShorterUrl(hash);
    }

    @GetMapping ("link-delete/{id}")
    public String deleteLink(@PathVariable("id") Long id) {
        shorterService.deleteById(id);
        return "redirect:/short-link/all-link";
    }

    @GetMapping("/all-link")
    public String findAll(Model model) {
        List<Shorter> shorts = shorterService.findAll();
        model.addAttribute("shorts", shorts);
        return "link-list";
    }
}
