package com.stp.app.service;

import com.stp.app.dto.Page;
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
        List<Movie> movieList = new ArrayList<>();
        movieRepository.findAll(pageable).forEach(movieList::add);
        return movieList;
    }

    public Movie getById(Integer id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElse(null);
    }

    public void addMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public Movie flagMovie(Integer id){
        Movie movie = getById(id);
        if(movie == null)
            return null;

        movie.updateFlags();
        addMovie(movie);

        return movie;
    }

    public List<Movie> getAllFlaged(){
        return movieRepository.findMovieByFlagsGreaterThan(0);
    }

    public void deleteMovieById(Integer id) {
        movieRepository.deleteById(id);
    }

    public void deleteAll(){
        movieRepository.deleteAll();
    }
}
