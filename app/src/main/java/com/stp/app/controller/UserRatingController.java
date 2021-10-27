package com.stp.app.controller;

import com.stp.app.dto.Rate;
import com.stp.app.entity.Movie;
import com.stp.app.service.UserRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRatingController {
    @Autowired
    private UserRatingService userRatingService;

    @RequestMapping(method = RequestMethod.POST, value = "/movies/{id}")
    public ResponseEntity<Movie> rateMovie(@PathVariable Integer id,
                                           @RequestHeader("Authorization") String header,
                                           @RequestBody Rate rate){

        Movie movie = userRatingService.rateMovie(id, header, rate);
        if(movie == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(movie);
    }
}
