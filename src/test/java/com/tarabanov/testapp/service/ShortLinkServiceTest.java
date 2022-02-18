package com.tarabanov.testapp.service;

import com.tarabanov.testapp.model.ShortLink;
import com.tarabanov.testapp.repository.LinkRepository;
import com.tarabanov.testapp.util.CodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class ShortLinkServiceTest {

    private ShortLinkService service;
    private LinkRepository repository;
    private CodeGenerator codeGenerator;

    private static final ShortLink SHORT_LINK1 = ShortLink.builder()
            .hash("1")
            .createdAt(ZonedDateTime.now())
            .originalUrl("https://mail.ru")
            .count(0L)
            .build();

    private static final ShortLink SHORT_LINK2 = ShortLink.builder()
            .hash("2")
            .createdAt(ZonedDateTime.now())
            .originalUrl("https://google.com")
            .count(0L)
            .build();

    private static final List<ShortLink> LINKS = Arrays.asList(SHORT_LINK1, SHORT_LINK2);

    static Stream<Arguments> shortLinkSource() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(ShortLink.builder().originalUrl(null).build()),
                Arguments.of(ShortLink.builder().originalUrl("").build())
        );
    }

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(LinkRepository.class);
        codeGenerator = Mockito.mock(CodeGenerator.class);
        service = new ShortLinkService(repository, codeGenerator); //Mockito.mock(ShortLinkService.class);
    }

    @Test
    void shouldReturnShortNotReturnNull() {

        when(repository.save(any())).thenReturn(SHORT_LINK1);
        ShortLink actual = service.generateShortUrl(SHORT_LINK1);
        assertEquals(actual, SHORT_LINK1);
    }

    @ParameterizedTest
    @MethodSource("shortLinkSource")
    void shouldReturnShortUrlNull(ShortLink shortLink) {
        assertNull(service.generateShortUrl(shortLink));
    }

    /**
     * тест для ShortLinkService# redirectShorterUrl
     * */
    @Test
    void shouldReturn404() {

        when(repository.findByHash(any())).thenReturn(null);
        ResponseEntity<String> actual = service.redirectShorterUrl(null);
        assertEquals(actual.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    /**
     * тест для ShortLinkService# redirectShorterUrl
     * */
    @Test
    void shouldReturnCorrectAnswer() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, SHORT_LINK1.getOriginalUrl());
        ResponseEntity<String> expected = new ResponseEntity<>(headers, HttpStatus.FOUND);

        when(repository.findByHash("1")).thenReturn(SHORT_LINK1);
        ResponseEntity<String> actual = service.redirectShorterUrl("1");
        assertEquals(expected, actual);
    }

    @Test
    void shouldInvokeRepositoryCorrectIfCountEqualsNull() {
        ShortLink link = ShortLink.builder().build();

        when(repository.findByHash("1"))
                .thenReturn(link);

        service.redirectShorterUrl("1");
        Mockito.verify(repository, Mockito.times(1)).save(link);
    }

    @Test
    void shouldInvokeRepositoryCorrectIfCountEqualsValue1() {
        ShortLink link = ShortLink.builder().count(1L).hash("2").build();

        when(repository.findByHash("2"))
                .thenReturn(link);

        service.redirectShorterUrl("2");
        Mockito.verify(repository, Mockito.times(1)).save(link);
    }

    @Test
    void deleteById() {
        Long id = 1L;
        service.deleteById(id);
        Mockito.verify(repository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void shouldReturnAllLinks() {
        when(repository.findAll()).thenReturn(LINKS);
        List<ShortLink> actual = service.findAll();
        assertEquals(LINKS, actual);
    }
}