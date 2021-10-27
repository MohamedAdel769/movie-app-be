package com.stp.app.service;

import com.stp.app.entity.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private MovieService movieService;

    @Mock
    private UserRatingService userRatingService;

    @InjectMocks
    private RecommendationService recommendationService;

    @Nested
    class recommendTests {
        @Test
        void When_GivenUserIsNull_Expect_Fail(){
            Mockito.when(userService.getByToken(anyString())).thenReturn(null);

            List<Movie> results = recommendationService.recommend("404", 5);
            assertNull(results);
        }

        @Test
        void When_GivenValidUserEmptyRatings_Expect_TopRatedWithLimit(){
            int limit = 2 ;
            User dummy = new User("dummy", "ok");
            dummy.setId(1);
            List<Movie> expected = Arrays.asList(
                    new Movie(1,"movie test 1", 1, 3.0),
                    new Movie(2,"movie test 2", 2, 2.0));
            List<UserRating> userRatings = new ArrayList<>();

            Mockito.when(userService.getByToken(anyString())).thenReturn(dummy);
            Mockito.when(userRatingService.getUserTopRated(anyInt())).thenReturn(userRatings);
            Mockito.when(movieService.getTopRated(anyInt())).thenReturn(expected);

            List<Movie> results = recommendationService.recommend("dummy", limit);
            assertThat(results).isEqualTo(expected);
        }

        @Test
        void When_GivenValidUserWithRatings_Expect_RecommendationsWithLimit(){
            int limit = 2;
            User dummy = new User("dummy", "ok");
            dummy.setId(1);
            List<Movie> UserRatedMovies = Arrays.asList(
                    new Movie(1,"movie test 1", 1, 3.0),
                    new Movie(2,"movie test 2", 2, 2.0),
                    new Movie(3,"movie test 3", 3, 1.0));

            List<Movie> otherMovies = Arrays.asList(
                    new Movie(4,"movie test 4", 1, 9.0),
                    new Movie(5,"movie test 5", 2, 8.0),
                    new Movie(6,"movie test 6", 3, 7.0));

            otherMovies.forEach(movie -> movie.setGenres(UserRatedMovies.get(0).getGenres()));

            List<UserRating> userRatings = Arrays.asList(
                    new UserRating(new UserRatingKey(1, 1), dummy, UserRatedMovies.get(0), 9.0, ""),
                    new UserRating(new UserRatingKey(1, 2), dummy, UserRatedMovies.get(1), 8.0, ""),
                    new UserRating(new UserRatingKey(1, 3), dummy, UserRatedMovies.get(2), 7.5, ""));

            Mockito.when(userService.getByToken(anyString())).thenReturn(dummy);
            Mockito.when(userRatingService.getUserTopRated(anyInt())).thenReturn(userRatings);
            Mockito.when(movieService.getAll()).thenReturn(otherMovies);

            List<Movie> expected = otherMovies.stream().limit(2).collect(Collectors.toList());

            List<Movie> results = recommendationService.recommend("dummy", limit);
            assertThat(results).isEqualTo(expected);
        }
    }

//    @Test
//    void getGenresOfUserTopRated(){
//        List<UserRating> userRatings = new ArrayList<>();
//
//        Set<Genre> results =  recommendationService.getGenresOfUserTopRated(userRatings);
//        assertEquals(0, results.size());
//    }
//
//    @Test
//    void getSimilarity(){
//        List<UserRating> userRatings = new ArrayList<>();
//        Set<Genre> genres = new HashSet<>();
//        Mockito.when(movieService.getAll()).thenReturn(new ArrayList<>());
//
//        Map<Integer, Movie> results =  recommendationService.getSimilarity(genres, userRatings);
//        assertEquals(0, results.size());
//    }
}
