package com.stp.app.controller;

import com.stp.app.entity.Movie;
import com.stp.app.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {

    @Autowired
    MovieService movieService;

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
