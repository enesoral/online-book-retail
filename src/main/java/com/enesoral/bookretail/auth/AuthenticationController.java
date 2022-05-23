package com.enesoral.bookretail.auth;

import com.enesoral.bookretail.tokenrefresh.RefreshTokenCommand;
import com.enesoral.bookretail.user.User;
import com.enesoral.bookretail.user.UserCommand;
import com.enesoral.bookretail.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(@Valid @RequestBody UserCommand userCommand) {
        return new ResponseEntity<>(userService.save(userCommand), HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public AuthenticationResponse signIn(@Valid @RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/refresh-token")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenCommand refreshTokenRequest) {
        return authenticationService.refreshToken(refreshTokenRequest);
    }
}
