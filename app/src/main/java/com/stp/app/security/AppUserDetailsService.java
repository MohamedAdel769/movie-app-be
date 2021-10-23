package com.stp.app.security;

import com.stp.app.entity.User;
import com.stp.app.repository.UserRepository;
import com.stp.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserDetailsService extends UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = getByEmail(s);

        user.orElseThrow(() -> new UsernameNotFoundException("User Not Found!"));

        return user.map(AppUserDetails::new).get();
    }
}
