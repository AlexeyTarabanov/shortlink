package com.tarabanov.testapp.repository;

import com.tarabanov.testapp.model.Shorter;
import org.springframework.data.repository.CrudRepository;


public interface ShorterRepository extends CrudRepository<Shorter, Long> {

    // метод поиска по hash- коду
    Shorter findByHash(String hash);
}
