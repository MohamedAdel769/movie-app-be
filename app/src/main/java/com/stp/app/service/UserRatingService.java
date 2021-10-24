package com.stp.app.service;

import com.stp.app.dto.Rate;
import com.stp.app.entity.Movie;
import com.stp.app.entity.User;
import com.stp.app.entity.UserRating;
import com.stp.app.entity.UserRatingKey;
import com.stp.app.repository.UserRatingRepository;
import com.stp.app.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRatingService {

    @Autowired
    private UserRatingRepository userRatingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private JwtUtil jwtUtil;

    public Movie updateMovieRating(Movie movie, Rate rate){
        Double old_avg = movie.getVoteAverage();
        Double new_avg = (old_avg * movie.getVoteCount() + rate.getRate()) / (movie.getVoteCount() + 1);
        movie.setVoteAverage(new_avg);
        movie.setVoteCount(movie.getVoteCount() + 1);
        movieService.addMovie(movie);

        return movie;
    }

    public Movie rateMovie(Integer movieId, String jwtToken, Rate rate){
        Movie movie = movieService.getById(movieId);
        String email = jwtUtil.extractUsernameByHeader(jwtToken);
        User user = userService.getByEmail(email).orElse(null);

        // if movie or user not found
        if(movie == null || user == null)
            return null;

        UserRatingKey userRatingKey = new UserRatingKey(user.getId(), movieId);
        UserRating userRating =  userRatingRepository.findById(userRatingKey).orElse(null);

        // if user didn't rate this movie before
        if(userRating == null){
            movie = updateMovieRating(movie, rate);
            userRating = new UserRating(userRatingKey, user, movie, rate.getRate(), rate.getDescription());
            userRatingRepository.save(userRating);

            //user.getRatedMovies().forEach(System.out::println);
        }

        return movie;
    }
}
