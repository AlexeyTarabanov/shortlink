package com.tarabanov.testapp.repository;

import com.tarabanov.testapp.model.Shorter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


public interface ShorterRepository extends JpaRepository<Shorter, Long> {

    // метод поиска по hash- коду
    Shorter findByHash(String hash);

    // Shorter
    void deleteShorterById(Long id);

}
