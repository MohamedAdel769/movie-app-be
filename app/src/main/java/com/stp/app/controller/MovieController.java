package com.stp.app.controller;

import com.stp.app.entity.Genre;
import com.stp.app.entity.Movie;
import com.stp.app.service.GenreService;
import com.stp.app.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class MovieController {

    @Autowired
    MovieService movieService;

    //<editor-fold desc="Test block">
//    @Autowired
//    GenreService genreService;
//    @RequestMapping("/test/{id}")
//    public Set<Movie> test(@PathVariable Integer id){
//        Genre genre = genreService.getById(id);
//        return genre.getMovies();
//    }
    //</editor-fold>

    @RequestMapping("/movies")
    public List<Movie> getAllMovies() {
        return movieService.getAll();
    }

    @RequestMapping("/movies/{id}")
    public Movie getMovie(@PathVariable Integer id){
        // TODO: What if movie not found ?
        return movieService.getById(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/admin/add/movie")
    public void addMovie(@RequestBody Movie movie) {
        movieService.addMovie(movie);
    }
}
