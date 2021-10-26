package com.stp.app.service;

import com.stp.app.entity.Movie;
import com.stp.app.entity.User;
import com.stp.app.repository.MovieRepository;
import com.stp.app.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieManagerService extends MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserService userService;

    public Movie flagMovie(Integer id, String token){
        Movie movie = getById(id);
        User user = userService.getByToken(token);

        if(movie == null || user == null)
            return null;

        movie.addUserFlag(user);

        //TODO: automatic hide?
        if(movie.getUsersFlagged().size() > 10)
            movie = hideTopFlagged(movie);

        userService.addUser(user);

        return addMovie(movie);
    }

    public List<Movie> getAllFlagged(){
        return movieRepository.findMovieByFlagsGreaterThan(0);
    }

    //TODO: reset flags when show movie?
    public Movie toggleHidden(Integer id){
        Movie movie = movieRepository.findById(id).orElse(null);
        if(movie == null)
            return null;

        movie.updateIsHidden();

        return addMovie(movie);
    }

    public Movie hideTopFlagged(Movie movie){
        if(!movie.getHidden())
            movie.updateIsHidden();

        return movie;
    }
}
