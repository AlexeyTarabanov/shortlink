package com.tarabanov.testapp.controller;

import com.tarabanov.testapp.model.ShortLink;
import com.tarabanov.testapp.service.ShortLinkService;
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
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    @GetMapping("/shorter-create")
    public String createShorterForm(ShortLink shortLink) {
        return "shorter-create";
    }

    // сохранение оригинальной ссылки и генерация короткого кода 'hash'
    @PostMapping(path = "/")
    public String createShortUrl(ShortLink shortLink, Model model) {
        ShortLink link = shortLinkService.generateShortUrl(shortLink);
        model.addAttribute("shortUrl", link);
        return "new-link";
    }

    // при переходе на нашу короткую ссылку перенаправляет пользователя на оригинальную ссылку
    @GetMapping("/{hash}")
    public ResponseEntity<String> redirectShorter(@PathVariable("hash") String hash) {
        return shortLinkService.redirectShorterUrl(hash);
    }

    @GetMapping ("link-delete/{id}")
    public String deleteLink(@PathVariable("id") Long id) {
        shortLinkService.deleteById(id);
        return "redirect:/short-link/all-link";
    }

    @GetMapping("/all-link")
    public String findAll(Model model) {
        List<ShortLink> links = shortLinkService.findAll();
        model.addAttribute("shorts", links);
        return "link-list";
    }
}
