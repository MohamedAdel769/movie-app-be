package com.stp.app.controller;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stp.app.dto.MovieDetails;
import com.stp.app.dto.Page;
import com.stp.app.entity.Movie;
import com.stp.app.security.AppUserDetailsService;
import com.stp.app.service.MovieService;
import com.stp.app.util.JwtUtil;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.NotExtensible;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
class MovieControllerTest {
    @MockBean
    private MovieService movieService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void When_GetAllWithPage_Expect_TargetPage() throws Exception {
        List<Movie> expected = Arrays.asList(
                new Movie(1,"movie test 1", 1, 3.0),
                new Movie(2,"movie test 2", 2, 2.0));

        Page page = new Page(2, 1);

        Mockito.when(movieService.getCatalog(any(Page.class))).thenReturn(expected);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(page))
                        .with(SecurityMockMvcRequestPostProcessors.user("user@gmail.com").password("1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expected.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void When_GetTopRated_TopRatedList() throws Exception {
        List<Movie> expected = Arrays.asList(
                new Movie(1,"movie test 1", 1, 3.0),
                new Movie(2,"movie test 2", 2, 2.0));

        Mockito.when(movieService.getTopRated(anyInt())).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movies/topRated")
                        .with(SecurityMockMvcRequestPostProcessors.user("user@gmail.com").password("1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expected.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Nested
    class getMovieTests {
        @Test
        void When_GivenMovieIdExists_Expect_TargetMovie() throws Exception {
            Movie expected = new Movie(1,"movie test 1", 1, 3.0);

            Mockito.when(movieService.getById(anyInt())).thenReturn(expected);

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/movies/{id}", 1)
                            .with(SecurityMockMvcRequestPostProcessors.user("user@gmail.com").password("1234")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("movie test 1"));
        }

        @Test
        void When_GivenMovieIsNull_Expect_Fail() throws Exception {
            Mockito.when(movieService.getById(anyInt())).thenReturn(null);

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/movies/{id}", -1)
                            .with(SecurityMockMvcRequestPostProcessors.user("user@gmail.com").password("1234")))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class addMovieControllerTests {
        @Test
        void When_AddMovieFromAdmin_Expect_AddedMovie() throws Exception {
            Movie expected = new Movie();
            expected.setId(1);

            Mockito.when(movieService.addMovie(any(Movie.class))).thenReturn(expected);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            mockMvc.perform(MockMvcRequestBuilders
                            .post("/admin/add/movie")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(expected))
                            .with(SecurityMockMvcRequestPostProcessors
                                    .user("admin")
                                    .password("admin")
                                    .authorities(List.of(new SimpleGrantedAuthority("ADMIN")))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        void When_AddMovieFromUser_Expect_forbidden() throws Exception {
            Movie expected = new Movie();
            expected.setId(1);

            Mockito.when(movieService.addMovie(any(Movie.class))).thenReturn(expected);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            mockMvc.perform(MockMvcRequestBuilders
                            .post("/admin/add/movie")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(expected))
                            .with(SecurityMockMvcRequestPostProcessors.user("user@gmail.com").password("1234")))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    class updateMovieControllerTests {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        @Test
        void When_GivenMovieIdExists_Expect_UpdatedMovie() throws Exception {
            Movie expected = new Movie(1,"movie test 1", 1, 3.0);

            Mockito.when(movieService.update(anyInt(), any(MovieDetails.class))).thenReturn(expected);

            mockMvc.perform(MockMvcRequestBuilders
                            .put("/admin/update/{movieId}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(new MovieDetails()))
                            .with(SecurityMockMvcRequestPostProcessors
                                    .user("admin")
                                    .password("admin")
                                    .authorities(List.of(new SimpleGrantedAuthority("ADMIN")))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("movie test 1"));
        }

        @Test
        void When_GivenMovieIsNull_Expect_Fail() throws Exception {
            Mockito.when(movieService.update(anyInt(), any(MovieDetails.class))).thenReturn(null);

            mockMvc.perform(MockMvcRequestBuilders
                            .put("/admin/update/{movieId}", -1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(new MovieDetails()))
                            .with(SecurityMockMvcRequestPostProcessors
                                    .user("admin")
                                    .password("admin")
                                    .authorities(List.of(new SimpleGrantedAuthority("ADMIN")))))
                    .andExpect(status().isNotFound());
        }
    }
}
