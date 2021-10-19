package com.stp.app.service;

import com.stp.app.entity.Genre;
import com.stp.app.entity.Movie;
import com.stp.app.repository.GenreRepository;
import com.stp.app.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public Genre getById(Integer id) {
        Optional<Genre> genre = genreRepository.findById(id);
        return genre.orElse(null);
    }

    public void addGenre(Genre genre) {
        genreRepository.save(genre);
    }
}
