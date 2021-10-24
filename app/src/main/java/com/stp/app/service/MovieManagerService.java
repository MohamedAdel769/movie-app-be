package com.stp.app.service;

import com.stp.app.entity.Movie;
import com.stp.app.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieManagerService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    public Movie flagMovie(Integer id){
        Movie movie = movieService.getById(id);
        if(movie == null)
            return null;

        movie.updateFlags();

        //TODO: automatic hide?
        if(movie.getFlags() > 10)
            movie = hideTopFlagged(movie);

        movieService.addMovie(movie);

        return movie;
    }

    public List<Movie> getAllFlagged(){
        return movieRepository.findMovieByFlagsGreaterThan(0);
    }

    //TODO: reset flags when show movie?
    public Movie toggleHidden(Integer id){
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isEmpty())
            return null;

        movie.get().updateIsHidden();
        movieService.addMovie(movie.get());

        return movie.get();
    }

    public Movie hideTopFlagged(Movie movie){
        //List<Movie> movieList = movieRepository.findMovieByFlagsGreaterThanAndIsHiddenFalse(10);
        //movieList.forEach(movie -> toggleHidden(movie.getId()));
        if(!movie.getHidden())
            movie.updateIsHidden();

        return movie;
    }
}
