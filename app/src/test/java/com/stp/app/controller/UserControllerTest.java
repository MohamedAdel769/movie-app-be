package com.stp.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.stp.app.dto.AuthRequest;
import com.stp.app.entity.Role;
import com.stp.app.entity.User;
import com.stp.app.security.AppUserDetails;
import com.stp.app.security.AppUserDetailsService;
import com.stp.app.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void When_LogInWithValidUser_Expect_JwtResponse() throws Exception {
        User user = new User("user@gmail.com", "1234");
        user.setRole(Role.USER);
        UserDetails userDetails = new AppUserDetails(user);
        String jwt = "jwt test.jwt.user";

        Mockito.when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        Mockito.when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn(jwt);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        AuthRequest authRequest = new AuthRequest(user.getEmail(), user.getPassword());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value(jwt));
    }

    @Test
    void When_LogInWithInValidUser_Expect_Fail() throws Exception {
        Mockito.when(userDetailsService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("User Not Found!"));
        Mockito.when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("404");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        AuthRequest authRequest = new AuthRequest("user@null", "13");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(authRequest)))
                .andExpect(status().isNotFound());
    }
}
