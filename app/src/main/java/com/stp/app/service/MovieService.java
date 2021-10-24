package com.stp.app.service;

import com.stp.app.dto.Page;
import com.stp.app.dto.Rate;
import com.stp.app.entity.Movie;
import com.stp.app.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.management.Query;
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

    public List<Movie> getAll(Page page) {
        Pageable pageable = PageRequest.of(page.getPageIndex()-1, page.getPageSize());
        return movieRepository.findAllByIsHiddenFalse(pageable);
    }

    public Movie getById(Integer id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        Movie movie = optionalMovie.orElse(null);
        if(movie == null || movie.getHidden())
            return null;
        return movie;
    }

    public void addMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public void deleteMovieById(Integer id) {
        movieRepository.deleteById(id);
    }

    public void deleteAll(){
        movieRepository.deleteAll();
    }
}
