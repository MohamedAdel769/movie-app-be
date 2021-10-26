package com.stp.app.service;

import com.stp.app.dto.MovieDetails;
import com.stp.app.dto.Page;
import com.stp.app.entity.Movie;
import com.stp.app.repository.MovieRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    void When_getAll_Expect_MovieList() {
        List<Movie> expected = new ArrayList<>();
        Mockito.when(movieRepository.findAll()).thenReturn(expected);
        List<Movie> actual = movieService.getAll();

        assertThat(actual).isEqualTo(expected);
    }

    @Nested
    class getCatalogTests {
        @Test
        void When_getCatalogByValidPage_Expect_TargetPage() {
            Page page = new Page(3, 1);
            Pageable pageable = PageRequest.of(page.getPageIndex()-1, page.getPageSize());
            List<Movie> expected = Arrays.asList(
                    new Movie(1,"movie test 1", 1, 3.0),
                    new Movie(2,"movie test 2", 2, 2.0),
                    new Movie(3,"movie test 3", 3, 1.0));

            Mockito.when(movieRepository.findAllByIsHiddenFalse(pageable)).thenReturn(expected);

            List<Movie> actual = movieService.getCatalog(page);

            assertEquals(pageable.getPageSize(), actual.size());
        }

        @Test
        void When_getCatalogByNullPage_Expect_DefaultPage() {
            Page page = spy(Page.class);
            int defaultIndex = 1;
            int defaultSize = 2;
            List<Movie> expected = Arrays.asList(
                    new Movie(1,"movie test 1", 1, 3.0),
                    new Movie(2,"movie test 2", 2, 2.0));

            lenient().when(page.getPageIndex()).thenReturn(defaultIndex);
            lenient().when(page.getPageSize()).thenReturn(defaultSize);
            Mockito.when(movieRepository.findAllByIsHiddenFalse(any(Pageable.class))).thenReturn(expected);

            List<Movie> actual = movieService.getCatalog(null);

            assertEquals(page.getPageSize(), actual.size());
        }
    }

    @Test
    void When_FindTopRatedAndLimitEqual2_Expect_TopRatedListOf2() {
        List<Movie> dummy = Arrays.asList(
                new Movie(1,"movie test 1", 1, 3.0),
                new Movie(2,"movie test 2", 2, 2.0),
                new Movie(3,"movie test 3", 3, 1.0)
        );
        final int LIMIT = 2;
        List<Movie> expected = dummy.stream().limit(LIMIT).collect(Collectors.toList());

        Mockito.when(movieRepository.findAllByIsHiddenFalseOrderByVoteAverageDesc()).thenReturn(dummy);

        List<Movie> actual = movieService.getTopRated(LIMIT);

        assertEquals(expected.size(), actual.size());

        for(int i=0;i<actual.size();i++){
            assertEquals(expected.get(i).getVoteAverage(), actual.get(i).getVoteAverage());
        }
    }

    @Nested
    class getByIdTests {
        @Test
        void When_IdIsValid_Expect_TargetMovie() {
            Movie expected = new Movie(1, "movie found", 0, 1.0);

            Mockito.when(movieRepository.findById(anyInt())).thenReturn(Optional.of(expected));

            Movie actual = movieService.getById(1);
            assertThat(actual).isEqualToComparingFieldByField(expected);
        }

        @Test
        void When_IdIsInValid_Expect_Null() {
            Mockito.when(movieRepository.findById(-1)).thenReturn(Optional.empty());

            Movie actual = movieService.getById(-1);
            assertNull(actual);
        }
    }

    @Nested
    class updateMovieTests {
        MovieDetails newMovieDetails = new MovieDetails("ar");

        @Test
        void When_UpdateNullMovie_Expect_Null(){
            MovieService movieServiceMock = mock(MovieService.class);
            lenient().when(movieServiceMock.getById(anyInt())).thenReturn(null);

            Movie actual = movieService.update(-1, newMovieDetails);
            assertNull(actual);
        }

        @Test
        void When_UpdateValidMovie_Expect_UpdatedMovie(){
            Movie movieSpy = spy(Movie.class);
            movieSpy.setId(1);
            movieSpy.setTitle("dummy");
            movieSpy.setVoteCount(0);
            movieSpy.setVoteAverage(1.0);

            Mockito.when(movieRepository.findById(1)).thenReturn(Optional.of(movieSpy));
            Mockito.when(movieRepository.save(movieSpy)).thenReturn(movieSpy);
            Movie actual = movieService.update(1, newMovieDetails);

            assertEquals(newMovieDetails.getLanguage(), actual.getOriginalLanguage());
            verify(movieSpy).setGenres(newMovieDetails.getGenres());
            verify(movieSpy).setReleaseDate(newMovieDetails.getReleaseDate());
            verify(movieSpy).setOriginalLanguage(newMovieDetails.getLanguage());
        }
    }

    @Nested
    class addMovieTests {
        @Test
        void When_AddNullMovie_Expect_Null(){
            Movie actual = movieService.addMovie(null);
            assertNull(actual);
        }

        @Test
        void When_AddValidMovie_Expect_AddedMovie(){
            Movie expected = new Movie(1,"movie test 1", 1, 3.0);

            Mockito.when(movieRepository.save(any(Movie.class))).thenReturn(expected);

            Movie actual = movieService.addMovie(expected);
            assertThat(actual).isEqualToComparingFieldByField(expected);
        }
    }

    @Test
    void deleteAll(){
        movieService.deleteAll();
        verify(movieRepository).deleteAll();
    }
}
