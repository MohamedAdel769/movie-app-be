package com.stp.app.controller;

import com.stp.app.dto.MovieDetails;
import com.stp.app.dto.Page;
import com.stp.app.dto.Rate;
import com.stp.app.entity.Genre;
import com.stp.app.entity.Movie;
import com.stp.app.entity.User;
import com.stp.app.service.*;
import com.stp.app.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    //<editor-fold desc="Test block">
//    @Autowired
//    GenreService genreService;
//    @RequestMapping("/test/{id}")
//    public Set<Movie> test(@PathVariable Integer id){
//        Genre genre = genreService.getById(id);
//        return genre.getMovies();
//    }
    //</editor-fold>

    @RequestMapping(method = RequestMethod.PUT, value = "/movies")
    public ResponseEntity<List<Movie>> getAllMovies(@RequestBody Page page) {
        return ResponseEntity.ok(movieService.getCatalog(page));
    }

    @RequestMapping("/movies/topRated")
    public ResponseEntity<List<Movie>> getTopRated() {
        return ResponseEntity.ok(movieService.getTopRated(15));
    }

    @RequestMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Integer id){
        Movie movie = movieService.getById(id);
        if(movie == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(movie);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/admin/add/movie")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        movie.setSource("admin");
        Movie newMovie = movieService.addMovie(movie);
        return ResponseEntity.ok(newMovie);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/admin/update/{movieId}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Integer movieId,
                                             @RequestBody MovieDetails movieDetails) {
        Movie movie = movieService.update(movieId, movieDetails);
        if(movie == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(movie);
    }
}
