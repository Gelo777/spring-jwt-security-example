package com.example.springjwtsecurityexample.service;

import com.example.springjwtsecurityexample.model.AppUserDetails;
import com.example.springjwtsecurityexample.model.AppUserPrincipal;
import com.example.springjwtsecurityexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserDetailsService {

    private final UserRepository userRepository;

    public Mono<UserDetails> findByUsername(String username){
        return userRepository.findByUsername(username).map(AppUserDetails::new);
    }
}
