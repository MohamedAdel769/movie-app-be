package com.stp.app.service;

import com.stp.app.entity.Movie;
import com.stp.app.entity.User;
import com.stp.app.repository.MovieRepository;
import com.stp.app.repository.UserRepository;
import com.stp.app.util.JwtUtil;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @Test
    void When_getAll_Expect_UserList() {
        List<User> expected = new ArrayList<>();
        Mockito.when(userRepository.findAll()).thenReturn(expected);
        List<User> actual = userService.getAll();

        assertThat(actual).isEqualTo(expected);
        verify(userRepository).findAll();
    }

    @Nested
    class getUserByIdTests {
        @Test
        void When_IdIsValid_Expect_TargetUser() {
            User expected = new User("dummy", "tuna");

            Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(expected));

            User actual = userService.getById(1);
            assertThat(actual).isEqualToComparingFieldByField(expected);
        }

        @Test
        void When_IdIsInValid_Expect_Null() {
            Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

            User actual = userService.getById(-1);
            assertNull(actual);
        }
    }

    @Nested
    class getUserByEmailTests {
        @Test
        void When_EmailIsValid_Expect_TargetUser() {
            User expected = new User("email", "tuna");

            Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(expected));

            User actual = userService.getByEmail("email").orElse(null);
            assertThat(actual).isEqualToComparingFieldByField(expected);
        }

        @Test
        void When_EmailIsInValid_Expect_Null() {
            Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

            User actual = userService.getByEmail("404").orElse(null);
            assertNull(actual);
        }
    }

    @Nested
    class addUserTests {
        @Test
        void When_AddNullUser_Expect_Fail(){
            User actual = userService.addUser(null);
            assertNull(actual);
        }

        @Test
        void When_AddValidUser_Expect_AddedUser(){
            User expected = new User("test", "tuna");

            Mockito.when(userRepository.save(any(User.class))).thenReturn(expected);

            User actual = userService.addUser(expected);
            assertThat(actual).isEqualToComparingFieldByField(expected);
        }
    }

    @Nested
    class getUserByTokenTests{
        @Test
        void When_GetByValidToken_Expect_TargetUser() {
            User expected = new User("email@test", "tuna");

            Mockito.when(jwtUtil.extractUsernameByHeader(anyString())).thenReturn(expected.getEmail());
            Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(expected));

            User actual = userService.getByToken("token");
            assertThat(actual).isEqualToComparingFieldByField(expected);
        }

        @Test
        void When_GetByInValidToken_Expect_Fail() {
            Mockito.when(jwtUtil.extractUsernameByHeader(anyString())).thenReturn("404");
            Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());


            User actual = userService.getByToken("token");
            assertNull(actual);
        }
    }
}
