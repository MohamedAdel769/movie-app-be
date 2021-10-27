package com.stp.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.stp.app.dto.Rate;
import com.stp.app.entity.Movie;
import com.stp.app.security.AppUserDetailsService;
import com.stp.app.service.UserRatingService;
import com.stp.app.util.JwtUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRatingController.class)
class UserRatingControllerTest {
    @MockBean
    private UserRatingService userRatingService;

    @MockBean
    private AppUserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class rateMovieTests {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        @Test
        void When_GivenMovieIdExists_Expect_RatedMovie() throws Exception {
            Movie expected = new Movie(1,"movie test 1", 1, 3.0);

            Mockito.when(userRatingService.rateMovie(anyInt(), anyString(), any(Rate.class))).thenReturn(expected);

            mockMvc.perform(MockMvcRequestBuilders
                            .post("/movies/{id}", 1)
                            .header("Authorization", "dummy")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(new Rate(3.0, "")))
                            .with(SecurityMockMvcRequestPostProcessors.user("user@gmail.com").password("1234")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("movie test 1"));
        }

        @Test
        void When_GivenMovieIsNull_Expect_Fail() throws Exception {
            Mockito.when(userRatingService.rateMovie(anyInt(), anyString(), any(Rate.class))).thenReturn(null);

            mockMvc.perform(MockMvcRequestBuilders
                            .post("/movies/{id}", -1)
                            .header("Authorization", "dummy")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(new Rate(1.0, "")))
                            .with(SecurityMockMvcRequestPostProcessors.user("user@gmail.com").password("1234")))
                    .andExpect(status().isNotFound());
        }
    }
}
