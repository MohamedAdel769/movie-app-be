package com.stp.app.service;

import com.stp.app.dto.Rate;
import com.stp.app.entity.Movie;
import com.stp.app.entity.User;
import com.stp.app.entity.UserRating;
import com.stp.app.entity.UserRatingKey;
import com.stp.app.repository.MovieRepository;
import com.stp.app.repository.UserRatingRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRatingServiceTest {
    @Mock
    private UserRatingRepository userRatingRepository;

    @Mock
    private UserService userService;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private UserRatingService userRatingService;

    @Test
    void When_UpdateMovieRate_Expect_RatedMovie() {
        Movie movieSpy = spy(Movie.class);
        movieSpy.setId(1);
        movieSpy.setTitle("dummy");
        movieSpy.setVoteCount(0);
        movieSpy.setVoteAverage(0.0);

        double rate = 8.0;
        Rate expected = mock(Rate.class);
        Mockito.when(expected.getRate()).thenReturn(rate);
        Mockito.when(movieService.addMovie(movieSpy)).thenReturn(movieSpy);

        Movie actual = userRatingService.updateMovieRating(movieSpy, expected);
        assertEquals(rate, actual.getVoteAverage());
        assertEquals(1, actual.getVoteCount());
    }

    @Nested
    class rateMovieTests {
        @Test
        void When_GivenMovieIsNull_Expect_Fail(){
            User dummy = new User("email@test", "tuna");
            Rate rate = new Rate(1.0, "dummy rate");

            Mockito.when(movieService.getById(anyInt())).thenReturn(null);
            Mockito.when(userService.getByToken(anyString())).thenReturn(dummy);

            Movie actual = userRatingService.rateMovie(-1, "dummy", rate);
            assertNull(actual);
        }

        @Test
        void When_GivenUserIsNull_Expect_Fail(){
            Movie dummy = new Movie(1, "movie dummy", 0, 1.0);
            Rate rate = new Rate(3.0, "dummy rate");

            Mockito.when(movieService.getById(anyInt())).thenReturn(dummy);
            Mockito.when(userService.getByToken(anyString())).thenReturn(null);

            Movie actual = userRatingService.rateMovie(1, "404", rate);
            assertNull(actual);
        }

        @Test
        void When_GivenUserAndUnRatedMovie_Expect_RatedMovie(){
            Movie movie = new Movie(1, "movie dummy", 0, 0.0);
            User user = new User("email@test", "tuna");
            Rate rate = new Rate(5.0, "dummy rate");

            Mockito.when(movieService.getById(anyInt())).thenReturn(movie);
            Mockito.when(userService.getByToken(anyString())).thenReturn(user);
            Mockito.when(userRatingRepository.findById(any(UserRatingKey.class))).thenReturn(Optional.empty());
            Mockito.when(movieService.addMovie(movie)).thenReturn(movie);

            Movie actual = userRatingService.rateMovie(1, "test", rate);
            assertEquals(rate.getRate(), actual.getVoteAverage());
            assertEquals(1, actual.getVoteCount());
        }

        @Test
        void When_GivenUserAndRatedMovie_Expect_NoChange(){
            Movie movie = new Movie(1, "movie dummy", 0, 0.0);
            User user = new User("email@test", "tuna");
            Rate rate = new Rate(5.0, "dummy rate");
            UserRating userRating = new UserRating();

            Mockito.when(movieService.getById(anyInt())).thenReturn(movie);
            Mockito.when(userService.getByToken(anyString())).thenReturn(user);
            Mockito.when(userRatingRepository.findById(any(UserRatingKey.class))).thenReturn(Optional.of(userRating));

            Movie actual = userRatingService.rateMovie(1, "test", rate);
            assertEquals(movie.getVoteAverage(), actual.getVoteAverage());
            assertEquals(0, actual.getVoteCount());
        }
    }

    @Test
    void When_getAll_Expect_UserTopRated() {
        List<UserRating> expected = new ArrayList<>();
        Mockito.when(userRatingRepository.findAllByUserIdOrderByRatingDesc(anyInt())).thenReturn(expected);
        List<UserRating> actual = userRatingService.getUserTopRated(1);

        assertThat(actual).isEqualTo(expected);
        verify(userRatingRepository).findAllByUserIdOrderByRatingDesc(1);
    }
}
