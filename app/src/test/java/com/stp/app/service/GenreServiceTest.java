package com.stp.app.service;

import com.stp.app.entity.Genre;
import com.stp.app.entity.Movie;
import com.stp.app.repository.GenreRepository;
import com.stp.app.repository.MovieRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {
    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    @Nested
    class getByIdTests {
        @Test
        void When_IdIsValid_Expect_TargetGenre() {
            Genre expected = new Genre(1, "test genre");

            Mockito.when(genreRepository.findById(anyInt())).thenReturn(Optional.of(expected));

            Genre actual = genreService.getById(1);
            assertThat(actual).isEqualToComparingFieldByField(expected);
        }

        @Test
        void When_IdIsInValid_Expect_Null() {
            Mockito.when(genreRepository.findById(-1)).thenReturn(Optional.empty());

            Genre actual = genreService.getById(-1);
            assertNull(actual);
        }
    }

    @Nested
    class addGenreTests {
        @Test
        void When_AddNullGenre_Expect_Null(){
            Genre actual = genreService.addGenre(null);
            assertNull(actual);
        }

        @Test
        void When_AddValidGenre_Expect_AddedGenre(){
            Genre expected = new Genre(1, "test genre");

            Mockito.when(genreRepository.save(any(Genre.class))).thenReturn(expected);

            Genre actual = genreService.addGenre(expected);
            assertThat(actual).isEqualToComparingFieldByField(expected);
        }
    }
}
