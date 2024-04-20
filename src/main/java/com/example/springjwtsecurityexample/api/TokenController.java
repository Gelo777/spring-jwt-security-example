package com.example.springjwtsecurityexample.api;

import com.example.springjwtsecurityexample.model.dto.PasswordTokenRequest;
import com.example.springjwtsecurityexample.model.dto.RefreshTokenRequest;
import com.example.springjwtsecurityexample.model.dto.TokenResponse;
import com.example.springjwtsecurityexample.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/public/token")
@RequiredArgsConstructor
public class TokenController {

    private final SecurityService securityService;

    @PostMapping("/password")
    public Mono<ResponseEntity<TokenResponse>> password(@RequestBody PasswordTokenRequest passwordTokenRequest) {
        return securityService.processPasswordToken(passwordTokenRequest.getUsername(), passwordTokenRequest.getPassword())
            .map(tokenData -> ResponseEntity.ok(new TokenResponse(tokenData.getToken(), tokenData.getRefreshToken())));
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<TokenResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        return securityService.processRefreshToken(request.getRefreshToken())
            .map(tokenData -> ResponseEntity.ok(new TokenResponse(tokenData.getToken(), tokenData.getRefreshToken())));
    }
}
