package com.enesoral.bookretail.auth;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AuthenticationResponse {

	private String email;

	private String authenticationToken;

	private String refreshToken;

	private LocalDateTime expiresAt;
}
