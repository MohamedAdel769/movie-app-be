package com.stp.app.controller;

import com.stp.app.dto.AuthRequest;
import com.stp.app.dto.AuthResponse;
import com.stp.app.entity.Movie;
import com.stp.app.security.AppUserDetailsService;
import com.stp.app.service.MovieService;
import com.stp.app.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MovieService movieService;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<AuthResponse> logIn(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (authRequest.getEmail(), authRequest.getPassword())
            );
        }catch (BadCredentialsException e){
            throw new Exception("Invalid username or password", e);
        }

        UserDetails userDetails = userDetailsService
                .loadUserByUsername(authRequest.getEmail());

        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @RequestMapping("/admin")
    public ResponseEntity<List<Movie>> getFlagged(){
        return ResponseEntity.ok(movieService.getAllFlagged());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/admin/{movieId}")
    public ResponseEntity<Movie> toggleHidden(@PathVariable Integer movieId){
        return ResponseEntity.ok(movieService.toggleHidden(movieId));
    }
}


