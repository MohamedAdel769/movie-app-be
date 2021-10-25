package com.stp.app.service;

import com.stp.app.entity.Movie;
import com.stp.app.entity.User;
import com.stp.app.repository.UserRepository;
import com.stp.app.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getById(Integer id){
        return userRepository.findById(id).orElse(null);
    }

    public Optional<User> getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User getByToken(String token){
        String email = jwtUtil.extractUsernameByHeader(token);

        return getByEmail(email).orElse(null);
    }
}
