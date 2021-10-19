package com.stp.app.repository;

import com.stp.app.entity.Genre;
import com.stp.app.entity.Movie;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre, Integer> {
}
