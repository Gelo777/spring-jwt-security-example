package com.example.springjwtsecurityexample.service;

import com.example.springjwtsecurityexample.model.User;
import com.example.springjwtsecurityexample.model.dto.TokenData;
import com.example.springjwtsecurityexample.model.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    public Mono<TokenData> processPasswordToken(String username, String password) {
        return userService.findByUsername(username)
            .flatMap(user -> {
                if (!passwordEncoder.matches(password, user.getPassword())) {
                    return Mono.error(new AuthException("Exception trying to check password for user: " + username));
                }

                return createTokenData(user);
            });
    }

    public Mono<TokenData> processRefreshToken(String refreshTokenValue) {
        return refreshTokenService.getByValue(refreshTokenValue)
            .flatMap(refreshToken -> userService.findById(refreshToken.getUserId()))
            .flatMap(this::createTokenData);
    }

    private Mono<TokenData> createTokenData(User user) {
        String token = tokenService.generateToken(
            user.getUsername(),
            user.getId(),
            user.getRoles().stream().map(Enum::name).toList());

        return refreshTokenService.save(user.getId())
            .flatMap(refreshToken -> Mono.just(new TokenData(token, refreshToken.getValue())));
    }


}
