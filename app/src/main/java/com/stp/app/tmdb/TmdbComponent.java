package com.stp.app.tmdb;

import com.stp.app.tmdb.dto.Genres;
import com.stp.app.tmdb.dto.TopRated;
import com.stp.app.entity.Genre;
import com.stp.app.entity.Movie;
import com.stp.app.service.GenreService;
import com.stp.app.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TmdbComponent {

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.toprated.url}")
    private String topRatedUrl;

    @Value("${api.genres.url}")
    private String genresUrl;

    private final Integer MAX_RESULTS = 100;

    @Autowired
    private GenreService genreService;

    @Autowired
    private MovieService movieService;

    private RestTemplate restTemplate;

    public TmdbComponent(){
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        restTemplate = restTemplateBuilder.build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        int catalogDbSize = movieService.getAll().size();
        if(catalogDbSize == 0){
            fetchAllGenres();
            fetchTopRated(0, 1);
        }
        else if(catalogDbSize < MAX_RESULTS){
            movieService.deleteAll();
            fetchTopRated(0, 1);
        }
    }

    public void fetchTopRated(int currentCount, int currentPage){
        String region = "EG";
        while (currentCount < MAX_RESULTS){
            String url = String.format("%s?api_key=%s&language=en-US&page=%d&region=%s",
                    topRatedUrl, apiKey, currentPage, region);

            TopRated topRated = restTemplate.getForObject(url, TopRated.class);
            if(topRated != null) {
                currentCount += topRated.getResults().size();
                currentPage++;
                topRated.getResults().stream().peek(
                                movie -> {
                                    Set<Genre> genres = new HashSet<>();
                                    movie.getGenres().forEach(genre -> {
                                        genres.add(genreService.getById(genre.getId()));
                                    });
                                    movie.setGenres(genres);

                                }).forEach(movie -> movieService.addMovie(movie));
            }
            else
                break;
        }
    }

    public void fetchAllGenres(){
        String url = String.format("%s?api_key=%s&language=en-US", genresUrl, apiKey);
        Genres genres = restTemplate.getForObject(url, Genres.class);
        genres.getGenres().forEach(genre -> genreService.addGenre(genre));
    }
}
