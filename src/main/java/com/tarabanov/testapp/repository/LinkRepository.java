package com.tarabanov.testapp.repository;

import com.tarabanov.testapp.model.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LinkRepository extends JpaRepository<ShortLink, Long> {

    ShortLink findByHash(String hash);

}
