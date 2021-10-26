package com.stp.app.controller;

import com.stp.app.entity.Movie;
import com.stp.app.entity.Role;
import com.stp.app.entity.User;
import com.stp.app.repository.MovieRepository;
import com.stp.app.repository.UserRatingRepository;
import com.stp.app.security.AppUserDetails;
import com.stp.app.security.AppUserDetailsService;
import com.stp.app.service.MovieManagerService;
import com.stp.app.service.MovieService;
import com.stp.app.service.UserRatingService;
import com.stp.app.service.UserService;
import com.stp.app.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(MovieController.class)
class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class MovieControllerTestContextConfiguration {
        @Bean
        public MovieService movieService() {
            return new MovieService();
        }

        @Bean
        public MovieManagerService movieManagerService() {
            return new MovieManagerService();
        }

        @Bean
        public UserRatingService userRatingService() {
            return new UserRatingService();
        }
    }

    @Autowired
    private MovieService movieService;

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    private AppUserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserRatingRepository userRatingRepository;

    @Test
    void getAllMovies() {
    }

    @Test
    void getTopRated() throws Exception {
        User user = new User("user@gmail.com", "1234");
        user.setRole(Role.USER);
        Mockito.when(jwtUtil.extractUsername(anyString())).thenReturn("user@gmail.com");
        Mockito.when(userDetailsService.loadUserByUsername(anyString())).thenReturn(new AppUserDetails(user));
        Mockito.when(jwtUtil.validateToken(anyString(), any(UserDetails.class))).thenReturn(true);

        MvcResult result = mockMvc.perform(
                get("http://localhost:8080/movies/topRated")
                .header("Authorization", "jwt eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGdtYWlsLmNvbSIsImV4cCI6MTYzNTUwODU1MywiaWF0IjoxNjM1MDc2NTUzfQ.dO4Mi_oY8Ak1mJWxknFLV4lq0UEyOD-43u59iZppMws"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(15, result.getResponse().getContentLength());
    }

    @Test
    void getMovie() {
    }

    @Test
    void flagMovie() {
    }

    @Test
    void rateMovie() {
    }
}
