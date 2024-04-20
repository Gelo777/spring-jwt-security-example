package com.example.springjwtsecurityexample.api;

import com.example.springjwtsecurityexample.model.RoleType;
import com.example.springjwtsecurityexample.model.User;
import com.example.springjwtsecurityexample.model.dto.CreateUserRequest;
import com.example.springjwtsecurityexample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/public/user")
@RequiredArgsConstructor
public class PublicUserController {

    private final UserService userService;

    @PostMapping
    public Mono<ResponseEntity<String>> createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .username(request.getUsername())
                .roles(request.getRoles().stream().map(role -> RoleType.valueOf(role.toUpperCase())).collect(Collectors.toSet())).build())
            .map(user -> ResponseEntity.ok("User crated"));
    }
}
