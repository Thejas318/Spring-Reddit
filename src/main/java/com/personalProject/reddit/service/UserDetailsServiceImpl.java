package com.personalProject.reddit.service;

import com.personalProject.reddit.Repository.UserRepository;
import com.personalProject.reddit.exceptionHandler.SpringRedditException;
import com.personalProject.reddit.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> userByName = userRepository.findByUserName(userName);
        User user = userByName
                .orElseThrow(() ->
                        new SpringRedditException("No user found with userName: " + userName));

        return new org.springframework.security
                .core.userdetails.User(user.getUserName(), user.getPassword(),
                user.isEnabled(), true, true, true,
                getAuthorities("USER"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return singletonList(new SimpleGrantedAuthority(role));
    }
}
