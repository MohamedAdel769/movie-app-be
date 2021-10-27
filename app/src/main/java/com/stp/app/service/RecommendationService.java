package com.stp.app.service;

import com.stp.app.entity.Genre;
import com.stp.app.entity.Movie;
import com.stp.app.entity.User;
import com.stp.app.entity.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserRatingService userRatingService;

    public List<Movie> recommend(String userToken, int LIST_SIZE){
        User user = userService.getByToken(userToken);

        if(user == null)
            return null;

        // get the top-rated movies by user
        List<UserRating> userRatings = userRatingService.getUserTopRated(user.getId());

        if(userRatings.size() == 0)
            return movieService.getTopRated(LIST_SIZE);

        // get all their genres
        Set<Genre> userGenres = getGenresOfUserTopRated(userRatings);

        // get intersections between each movie genres and the user genres
        Map<Integer, Movie> topSimilarities = getSimilarity(userGenres, userRatings);

        // return only the first [LIST_SIZE] movies
        return topSimilarities.values().stream()
                .limit(LIST_SIZE).collect(Collectors.toList());
    }

    public Set<Genre> getGenresOfUserTopRated(List<UserRating> userRatings){
        Set<Genre> genreSet = new HashSet<>();

        userRatings.forEach(userRating -> {
            if (userRating.getRating() > 7.0)
                genreSet.addAll(userRating.getMovie().getGenres());
        });

        return genreSet;
    }

    public Map<Integer, Movie> getSimilarity(Set<Genre> userGenres,
                                               List<UserRating> userRatings){
        Map<Integer, Movie> similarities = new TreeMap<>(Collections.reverseOrder());
        List<Movie> otherMovies = new ArrayList<>();
        Set<Integer> watchedMovies = new HashSet<>();

        // get movies user already rated [watched]
        userRatings.forEach(
                userRating -> watchedMovies.add(userRating.getMovie().getId())
        );

        // get top-rated movies excluding watched ones
        List<Movie> movieList = movieService.getAll();
        movieList.forEach(
                movie -> {
                    if(!watchedMovies.contains(movie.getId()) && !movie.getHidden()
                            && movie.getVoteAverage() > 7.0)
                        otherMovies.add(movie);
                }
        );

        // calculate similarity for each movie and put it in a map sorted desc
        otherMovies.forEach(movie -> {
            int similarity = Sets.intersection(movie.getGenres(), userGenres).size();
            if(similarity > 0) {
                if(!similarities.containsKey(similarity))
                    similarities.put(similarity, movie);
                else{
                    if(movie.getVoteAverage() > similarities.get(similarity).getVoteAverage())
                        similarities.put(similarity+1, movie);
                    else
                        similarities.put(similarity-1, movie);
                }
            }
        });

        return similarities;
    }
}
