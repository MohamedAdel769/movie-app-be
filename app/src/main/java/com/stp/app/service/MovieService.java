package com.stp.app.service;

import com.stp.app.entity.Movie;
import com.stp.app.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAll() {
        List<Movie> movieList = new ArrayList<>();
        movieRepository.findAll().forEach(movieList::add);
        return movieList;
    }

    public Movie getById(Integer id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElse(null);
    }

    public void addMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public void deleteMovieById(Integer id) {
        movieRepository.deleteById(id);
    }
}
