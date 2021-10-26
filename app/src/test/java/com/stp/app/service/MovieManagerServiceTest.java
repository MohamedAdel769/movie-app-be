package com.stp.app.service;

import com.stp.app.entity.Movie;
import com.stp.app.entity.User;
import com.stp.app.repository.MovieRepository;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieManagerServiceTest {
    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserService userService;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieManagerService movieManagerService;

    @Nested
    class flagMovieTests {
        @Test
        void When_GivenMovieIsNull_Expect_Fail(){
            User dummy = new User("dummu", "tuna");

            Mockito.when(movieRepository.findById(anyInt())).thenReturn(Optional.empty());
            Mockito.when(userService.getByToken(anyString())).thenReturn(dummy);

            Movie actual = movieManagerService.flagMovie(-1, "dummy");
            assertNull(actual);
        }

        @Test
        void When_GivenUserIsNull_Expect_Fail(){
            Movie dummy = new Movie(1,"dummy", 1, 3.0);

            Mockito.when(movieRepository.findById(anyInt())).thenReturn(Optional.of(dummy));
            Mockito.when(userService.getByToken(anyString())).thenReturn(null);

            Movie actual = movieManagerService.flagMovie(1, "null");
            assertNull(actual);
        }

        @Test
        void When_GivenValidMovieAndUser_Expect_FlaggedMovie(){
            int oldFlags = 0;
            Movie movieSpy = spy(Movie.class);
            movieSpy.setId(1);
            movieSpy.setTitle("dummy");
            movieSpy.setVoteCount(0);
            movieSpy.setVoteAverage(1.0);
            movieSpy.setFlags(oldFlags);
            User user = new User("user", "tuna");

            Mockito.when(movieRepository.findById(anyInt())).thenReturn(Optional.of(movieSpy));
            Mockito.when(userService.getByToken(anyString())).thenReturn(user);
            lenient().when(movieRepository.save(movieSpy)).thenReturn(movieSpy);

            Movie actual = movieManagerService.flagMovie(1, "null");
            assertEquals(oldFlags+1, actual.getFlags());
            assertThat(actual).isEqualToComparingFieldByField(movieSpy);
        }

    }

    @Test
    void When_GetFlaggedMovies_Expect_FlaggedList() {
        List<Movie> expected = new ArrayList<>();
        Mockito.when(movieRepository.findMovieByFlagsGreaterThan(anyInt())).thenReturn(expected);

        List<Movie> actual = movieManagerService.getAllFlagged();
        verify(movieRepository).findMovieByFlagsGreaterThan(0);
        assertThat(actual).isEqualTo(expected);
    }

    @Nested
    class toggleHiddenTests {
        @Test
        void When_HideGivenMovieIsNull_Expect_Fail(){
            Mockito.when(movieRepository.findById(anyInt())).thenReturn(Optional.empty());

            Movie actual = movieManagerService.toggleHidden(-1);
            assertNull(actual);
        }

        @Test
        void When_HideValidMovie_Expect_HiddenMovie(){
            Movie movieSpy = spy(Movie.class);
            movieSpy.setId(1);
            movieSpy.setTitle("dummy");
            movieSpy.setVoteCount(0);
            movieSpy.setVoteAverage(1.0);
            movieSpy.setHidden(false);

            Mockito.when(movieRepository.findById(anyInt())).thenReturn(Optional.of(movieSpy));
            lenient().when(movieRepository.save(movieSpy)).thenReturn(movieSpy);

            Movie actual = movieManagerService.toggleHidden(1);
            assertTrue(actual.getHidden());
        }
    }

    @Nested
    class AutoHideTopFlaggedTests{
        @Test
        void When_MovieFlagsGreaterThan10_Expect_AutoHiding(){
            Movie movieSpy = spy(Movie.class);
            movieSpy.setId(1);
            movieSpy.setTitle("dummy");
            movieSpy.setVoteCount(0);
            movieSpy.setVoteAverage(1.0);
            movieSpy.setHidden(false);
            User user = new User("user", "tuna");

            Mockito.when(movieRepository.findById(anyInt())).thenReturn(Optional.of(movieSpy));
            Mockito.when(userService.getByToken(anyString())).thenReturn(user);
            Mockito.when(movieSpy.getFlags()).thenReturn(11);
            lenient().when(movieRepository.save(movieSpy)).thenReturn(movieSpy);

            Movie actual = movieManagerService.flagMovie(1, "null");

            assertTrue(actual.getHidden());
        }

        @Test
        void When_MovieFlagsGreaterThan10AndHidden_Expect_NoChange(){
            Movie movieSpy = spy(Movie.class);
            movieSpy.setId(1);
            movieSpy.setTitle("dummy");
            movieSpy.setVoteCount(0);
            movieSpy.setVoteAverage(1.0);
            movieSpy.setHidden(true);
            User user = new User("user", "tuna");

            Mockito.when(movieRepository.findById(anyInt())).thenReturn(Optional.of(movieSpy));
            Mockito.when(userService.getByToken(anyString())).thenReturn(user);
            lenient().when(movieSpy.getFlags()).thenReturn(11);
            lenient().when(movieRepository.save(movieSpy)).thenReturn(movieSpy);

            Movie actual = movieManagerService.flagMovie(1, "null");

            verify(movieSpy, never()).updateIsHidden();
        }
    }
}
