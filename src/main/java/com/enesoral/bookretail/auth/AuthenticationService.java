package com.enesoral.bookretail.auth;

import com.enesoral.bookretail.jwt.JwtTokenProvider;
import com.enesoral.bookretail.tokenrefresh.RefreshTokenCommand;
import com.enesoral.bookretail.tokenrefresh.RefreshTokenService;
import com.enesoral.bookretail.user.UserCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenService refreshTokenService;

    private final AuthenticationManager authenticationManager;

    private final Long expiryInMs;

    public AuthenticationService(JwtTokenProvider jwtTokenProvider,
                                 RefreshTokenService refreshTokenService,
                                 AuthenticationManager authenticationManager,
                                 @Value("${jwt.expiryInMs:3600000}") Long expiryInMs) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.expiryInMs = expiryInMs;
    }

    public AuthenticationResponse authenticate(UserCommand user) {
        final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        final List<String> roles = authenticate.getAuthorities().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        final String token = jwtTokenProvider.createToken(user.getEmail(), roles);
        return getAuthenticationResponse(user, token);
    }

    public AuthenticationResponse refreshToken(RefreshTokenCommand refreshTokenCommand) {
        refreshTokenService.validateRefreshToken(refreshTokenCommand.getRefreshToken());
        final String token = jwtTokenProvider.generateTokenWithUserName(refreshTokenCommand.getEmail());
        refreshTokenService.deleteRefreshToken(refreshTokenCommand.getRefreshToken());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenCommand.getRefreshToken())
                .expiresAt(LocalDateTime.now().plusSeconds(expiryInMs / 1000))
                .email(refreshTokenCommand.getEmail())
                .build();
    }

    private AuthenticationResponse getAuthenticationResponse(UserCommand user, String token) {
        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .authenticationToken(token)
                .expiresAt(LocalDateTime.now().plusSeconds(expiryInMs / 1000))
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .build();
    }

}
