package com.stp.app.controller;

import com.stp.app.entity.Movie;
import com.stp.app.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @RequestMapping("/user/recommendations")
    public ResponseEntity<List<Movie>> getRecommendations(@RequestHeader("Authorization") String header){
        List<Movie> recommendations = recommendationService.recommend(header, 5);
        if(recommendations == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(recommendations);
    }
}
