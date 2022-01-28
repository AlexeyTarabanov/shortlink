package com.tarabanov.testapp.repository;

import com.tarabanov.testapp.model.Shorter;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShorterRepository extends JpaRepository<Shorter, Long> {

    Shorter findByHash(String hash);

    void deleteShorterById(Long id);

}
