package com.stp.app.controller;

import com.stp.app.entity.Movie;
import com.stp.app.service.MovieManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MovieManagerController {

    @Autowired
    private MovieManagerService movieManagerService;

    @RequestMapping(method = RequestMethod.PUT, value = "/movies/{id}")
    public ResponseEntity<?> flagMovie(@RequestHeader("Authorization") String header,
                                       @PathVariable Integer id){
        Movie movie = movieManagerService.flagMovie(id, header);
        if(movie == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(movie);
    }
}
