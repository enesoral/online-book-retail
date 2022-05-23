package com.enesoral.bookretail.auth;

import com.enesoral.bookretail.tokenrefresh.RefreshTokenCommand;
import com.enesoral.bookretail.user.UserCommand;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/sign-in")
    public AuthenticationResponse signIn(@Valid @RequestBody UserCommand user) {
        return authenticationService.authenticate(user);
    }

    @PostMapping("/refresh-token")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenCommand refreshTokenRequest) {
        return authenticationService.refreshToken(refreshTokenRequest);
    }

}
