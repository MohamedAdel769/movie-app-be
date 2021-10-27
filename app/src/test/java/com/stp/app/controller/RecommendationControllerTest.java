package com.stp.app.controller;

import com.stp.app.entity.Movie;
import com.stp.app.security.AppUserDetailsService;
import com.stp.app.service.RecommendationService;
import com.stp.app.util.JwtUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendationController.class)
class RecommendationControllerTest {

    @MockBean
    private RecommendationService recommendationService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @Nested
    class getRecommendationsTests {

        @Test
        void When_GivenUserExists_Expect_RecommendationsList() throws Exception {
            List<Movie> expected = List.of(new Movie(1,"movie test 1", 1, 3.0));

            Mockito.when(recommendationService.recommend(anyString(), anyInt())).thenReturn(expected);

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/user/recommendations")
                            .header("Authorization", "dummy")
                            .with(SecurityMockMvcRequestPostProcessors.user("user@gmail.com").password("1234")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(expected.size()))
                    .andExpect(jsonPath("$[0].id").value(1));
        }

        @Test
        void When_GivenMovieIsNull_Expect_Fail() throws Exception {
            Mockito.when(recommendationService.recommend(anyString(), anyInt())).thenReturn(null);

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/user/recommendations")
                            .header("Authorization", "dummy")
                            .with(SecurityMockMvcRequestPostProcessors.user("user@gmail.com").password("1234")))
                    .andExpect(status().isNotFound());
        }
    }
}
