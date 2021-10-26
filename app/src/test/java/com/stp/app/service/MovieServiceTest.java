package com.stp.app.service;

import com.stp.app.entity.Movie;
import com.stp.app.repository.MovieRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    void getAll() {
    }

    @Test
    void getCatalog() {
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
    class getById {
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

    @Test
    void update() {
    }

    @Test
    void addMovie() {
    }

    @Test
    void deleteAll() {
    }
}
